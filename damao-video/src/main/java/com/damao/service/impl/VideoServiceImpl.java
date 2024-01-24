package com.damao.service.impl;

import com.damao.constant.AuditConstant;
import com.damao.constant.RedisNameConstant;
import com.damao.context.BaseContext;
import com.damao.result.exception.BaseException;
import com.damao.mapper.VideoMapper;
import com.damao.mapper.VideoResourceMapper;
import com.damao.pojo.entity.Video;
import com.damao.pojo.entity.VideoLikesRelation;
import com.damao.pojo.entity.VideoResource;
import com.damao.pojo.vo.VideoRankVO;
import com.damao.service.VideoAuditService;
import com.damao.service.VideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    VideoMapper videoMapper;

    @Autowired
    VideoResourceMapper videoResourceMapper;

    @Autowired
    VideoAuditService videoAuditService;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    /**
     * 发布视频，审核
     */
    @Override
    public Long publish(Video video) {
        video.setOwnerId(BaseContext.getCurrentId());
        video.setLikes(0);
        video.setShares(0);
        video.setAuditStatus(0);
        video.setAuditResult(0);
        video.setSaves(0);
        video.setAuditResult(AuditConstant.INIT_PASS_STATUS);
        //可以生成主键vid
        videoMapper.save(video);

        VideoResource videoResource = VideoResource.builder()
                .vid(video.getVid())
                .videoUrl(video.getVideoUrl())
                .build();

        videoResourceMapper.save(videoResource);
        // 进行一个异步审核
        videoAuditService.audit(video);
        return video.getVid();
    }

    @Override
    public void update(Video video) {
        videoMapper.update(video);
    }

    @Override
    public Video getVideoById(Long vid) {
        Video video = videoMapper.getVideoById(vid);
        return video;
    }

    @Override
    public List<VideoRankVO> getDailyTopTenViewVideos() {
        Set<Object> videoIds = redisTemplate.opsForZSet().reverseRange(RedisNameConstant.VIDEO_DAILY_VIEW_RANK, 0, 9);

        if (videoIds == null || videoIds.isEmpty()) {
            throw new BaseException("当前没有视频排行");
        }
        List<VideoRankVO> result = new ArrayList<>();
        List<Long> vidList = new ArrayList<>();

        for (Object obj : videoIds){
            vidList.add((Long) obj);
        }
        List<Video> results = videoMapper.getByIds(vidList);

        for (Video video : results) {
            Double score = redisTemplate.opsForZSet().score(RedisNameConstant.VIDEO_DAILY_VIEW_RANK, video.getVid());
            VideoRankVO videoRankVO = new VideoRankVO();
            BeanUtils.copyProperties(video, videoRankVO);
            videoRankVO.setViews(score);
            result.add(videoRankVO);
        }
        return result;
    }

    /**
     * 用以数据迁移，不必判断缓存
     */
    @Override
    public void updateLikeRelation(VideoLikesRelation videoLikesRelation) {
        videoMapper.updateLikeRelation(videoLikesRelation);
    }

    /**
     * 用以数据迁移，不必判断缓存
     */
    @Override
    public VideoLikesRelation getLikeRelation(Long uid, Long vid) {
        return videoMapper.getLikeRelation(uid, vid);
    }

    /**
     * 用以数据迁移，不必判断缓存
     */
    @Override
    public void saveLikeRelation(VideoLikesRelation videoLikesRelation) {
        videoMapper.saveLikeRelation(videoLikesRelation);
    }


    /**
     * 用户获取视频点赞数量，先尝试获取缓存，不存在再从DB中加载到缓存并返回
     */
    @Override
    public Integer getVideoLikesCount(Long vid) {
        Object count = redisTemplate.opsForHash().get(RedisNameConstant.VIDEO_LIKES_COUNT, vid.toString());
        if (count == null) {
            Video video = videoMapper.getVideoById(vid);
            if (video == null) throw new BaseException("视频不存在");
            count = video.getLikes();
            redisTemplate.opsForHash().put(RedisNameConstant.VIDEO_LIKES_COUNT, vid.toString(), count);
        }
        return (Integer) count;
    }

    /**
     * 用户点赞业务，实现了保存点赞关系并且同步增减点赞数量
     */
    @Override
    public void saveLikeRelationUser(Long vid, Integer status) {
        // 如果状态相同，则请求错误，应该直接返回。
        if (getLikesStatus(vid).equals(status)) throw new BaseException("请勿重复操作哦");
        if (status != 1 && status != 0) {
            throw new BaseException("点赞状态字段不正确");
        }
        // 作用是加载点赞数量到缓存
        getVideoLikesCount(vid);

        if (status == 0) { // 说明要加一个赞
            redisTemplate.opsForHash().increment(RedisNameConstant.VIDEO_LIKES_COUNT, vid.toString(), -1);
        } else { // 说明要减一个赞
            redisTemplate.opsForHash().increment(RedisNameConstant.VIDEO_LIKES_COUNT, vid.toString(), 1);
        }
        // Hash数据结构保证不会重复点赞
        redisTemplate.opsForHash().put(RedisNameConstant.VIDEO_LIKES_RECORD, "" + BaseContext.getCurrentId() + ":" + vid, status);
    }

    /**
     * 获取用户点赞状态，先查缓存，不存在就从数据库查，数据库不存在也要返回0
     * 表示未点赞，存在则加载缓存并返回结果。注意，如果数据库中不存在，不会将
     * 信息加载到缓存。
     */
    @Override
    public Integer getLikesStatus(Long vid) {
        Object value = redisTemplate.opsForHash().get(RedisNameConstant.VIDEO_LIKES_RECORD, "" + BaseContext.getCurrentId() + ":" + vid);
        if (value != null) {
            return (Integer) value;
        }
        VideoLikesRelation videoLikesRelation = videoMapper.getLikeRelation(BaseContext.getCurrentId(), vid);
        if (videoLikesRelation == null) {
            return 0;
        }
        Integer status = videoLikesRelation.getStatus();
        redisTemplate.opsForHash().put(RedisNameConstant.VIDEO_LIKES_RECORD, "" + BaseContext.getCurrentId() + ":" + vid, status);
        return status;
    }
}
