package com.damao.schedul;

import com.damao.constant.RedisNameConstant;
import com.damao.pojo.entity.Video;
import com.damao.pojo.entity.VideoLikesRelation;
import com.damao.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.Set;


@Component
@Slf4j
public class TaskManager {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    VideoService videoService;

    /**
     * 每天0点执行
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanViewCountHistory() {
        log.info("定时任务启动，删除当日浏览记录排行记录[]~(￣▽￣)~*");
        try {
            Set<ZSetOperations.TypedTuple<Object>> obj = redisTemplate.opsForZSet().rangeWithScores(RedisNameConstant.VIDEO_DAILY_VIEW_RANK,0,0);
            if (obj != null && !obj.isEmpty()) {
                redisTemplate.opsForZSet().remove(RedisNameConstant.VIDEO_DAILY_VIEW_RANK);
            }
        }
        catch (RedisSystemException e) {
            e.printStackTrace();
        }
    }

    /**
     * 每五分钟执行一次，作用是迁移用户点赞缓存入库
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void saveVideoLikes2DB() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisNameConstant.VIDEO_LIKES_RECORD, ScanOptions.NONE);
        int cnt = 0;
        while (cursor.hasNext()) {
            cnt ++;
            Map.Entry<Object, Object> entry = cursor.next();
            String key = (String) entry.getKey();
            String[] split = key.split(":");
            Long uid = Long.valueOf(split[0]);
            Long vid = Long.valueOf(split[1]);
            Integer value = (Integer) entry.getValue();
            VideoLikesRelation videoLikesRelation = videoService.getLikeRelation(uid,vid);
            if (videoLikesRelation == null) {
                videoLikesRelation = VideoLikesRelation.builder()
                        .uid(uid)
                        .vid(vid)
                        .status(value)
                        .build();
                System.out.println(videoLikesRelation.getStatus());
                videoService.saveLikeRelation(videoLikesRelation);
            }
            else {
                videoLikesRelation.setStatus(value);
                videoService.updateLikeRelation(videoLikesRelation);
            }
            /*
             *  持久化完毕即可删除
             */
            redisTemplate.opsForHash().delete(RedisNameConstant.VIDEO_LIKES_RECORD, key);
        }
        log.info("成功迁移视频点赞关系到数据库ᓚᘏᗢ,迁移条数：{}",cnt);
    }
    /**
     * 每五分钟执行一次，作用是迁移视频点赞数量入库
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void saveVideoLikesCount2DB() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisNameConstant.VIDEO_LIKES_COUNT, ScanOptions.NONE);
        int cnt = 0;
        while (cursor.hasNext()) {
            cnt ++;
            Map.Entry<Object, Object> entry = cursor.next();
            String key = (String) entry.getKey();
            Integer value = (Integer) entry.getValue();
            Video video = videoService.getVideoById(Long.valueOf(key));
            video.setLikes(value);
            videoService.update(video);
            /*
             *  持久化完毕即可删除
             */
            redisTemplate.opsForHash().delete(RedisNameConstant.VIDEO_LIKES_COUNT, key);
        }
        log.info("成功迁移视频点赞数量到数据库（￣︶￣）↗　,迁移条数：{}",cnt);
    }

    @PreDestroy
    public void saveAll2DB() {
        saveVideoLikes2DB();
        saveVideoLikesCount2DB();
    }
}
