package com.damao.service.impl;

import com.damao.constant.RedisNameConstant;
import com.damao.context.BaseContext;
import com.damao.pojo.entity.User;
import com.damao.pojo.vo.FeedItemVO;
import com.damao.pojo.vo.HomeFeedVO;
import com.damao.service.UserService;
import com.damao.utils.RandomUtil;
import com.damao.mapper.VideoMapper;
import com.damao.pojo.entity.Video;
import com.damao.service.VideoRecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VideoRecommendServiceImpl implements VideoRecommendService {

    @Autowired
    VideoMapper videoMapper;

    @Autowired
    UserService userService;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<Video> get() {
        Long uid = BaseContext.getCurrentId();
        if (uid == null) {
            uid = 11L;
        }
        /* TODO 完成视频推荐逻辑 */
        // List<Video> res = videoMapper.getRandFour();
        // 以下是根据兴趣推送
        List<Video> res = null;

        Map<?, ?> userMap = (Map<?, ?>) redisTemplate.opsForHash().get(RedisNameConstant.USER_MODEL, String.valueOf(uid));
        Long[] userVector = null;
        // 用户模型不存在, 放入模型
        if (userMap == null) {
            String model_s = videoMapper.getModel(uid);
            String[] model = model_s.substring(1, model_s.length() - 1).split(",");
            Long[] vector = new Long[10];
            for (int i = 0; i < 10; i++) {
                vector[i] = Long.parseLong(model[i]);
            }
            userVector = vector;
            Map<String, Long[]> map = new HashMap<>();
            map.put("model", vector);
            // 注意序列化的方式为String
            redisTemplate.opsForHash().put(RedisNameConstant.USER_MODEL, String.valueOf(uid), map);
        } else {
            userVector = (Long[]) userMap.get("model");
        }

        int[] numberOfTypes = RandomUtil.randomByWeight(userVector, 10);
        List<Long> ids = new ArrayList<>();

        int typeIndex = 1;
        for (int number : numberOfTypes) {
            List<Object> vidList = redisTemplate.opsForSet().randomMembers(String.valueOf(typeIndex), number);
            assert vidList != null;
            for (Object vid : vidList) {
                // 注意JSON自动降低精度情况，要使用Number来映射
                ids.add(((Number) vid).longValue());
            }
            typeIndex++;
        }

        res = videoMapper.getByIds(ids);
        return res;
    }

    @Override
    public List<Video> recommendSimilar(Long vid) {
        redisTemplate.opsForHash();
        return null;
    }

    @Override
    public List<FeedItemVO> feed() {
        List<Long> list = new ArrayList<>();
        list.add(113L);
        list.add(114L);
        list.add(115L);
        list.add(116L);
        List<Video> videos = videoMapper.selectBatchIds(list);
        List<FeedItemVO> feedItemVOList = new ArrayList<>();
        List<Long> uidList = videos.stream().map(Video::getOwnerId).toList();
        List<Long> vidList = videos.stream().map(Video::getVid).toList();
        List<User> authors = userService.getByIds(uidList);
        Map<Long, User> authorMap = authors.stream().collect(Collectors.toMap(User::getUid, a -> a, (v1, v2) -> v1));
        for (Video video : videos) {
            FeedItemVO feedItemVO = new FeedItemVO();
            FeedItemVO.Video videoVO = new FeedItemVO.Video();
            FeedItemVO.Author authorVO = new FeedItemVO.Author();
            User user = authorMap.get(video.getOwnerId());
            // 组装作者信息
            authorVO.setNickname(user.getUsername());
            authorVO.setUid(String.valueOf(user.getUid()));
            authorVO.setHeadImg(user.getAvatar());
            authorVO.setFollowStatus(1L);
            // 组装videoVO
            videoVO.setCover(video.getVideoCoverUrl());
            videoVO.setSrc(video.getVideoUrl());
            videoVO.setTitle(video.getVideoName());
            videoVO.setPublishTime(video.getCreateTime());
            videoVO.setSaveStatus(false);
            videoVO.setLikeStatus(false);
            videoVO.setTags("这是taghhh 这是taghhh");
            // 组装统计数据
            FeedItemVO.Statistics statisticsVO = new FeedItemVO.Statistics();
            statisticsVO.setCommentCount(12L);
            statisticsVO.setPlayCount(1231L);
            statisticsVO.setLikeCount(1231L);
            statisticsVO.setShareCount(447L);
            statisticsVO.setSaveCount(114L);

            feedItemVO.setVideo(videoVO);
            feedItemVO.setAuthor(authorVO);
            feedItemVO.setStatistics(statisticsVO);
            feedItemVO.setFeedType(1L);
            feedItemVO.setFeedItemId(video.getVid());
            feedItemVOList.add(feedItemVO);
        }
        return feedItemVOList;
    }

    @Override
    public List<HomeFeedVO> homeFeed() {
        List<Long> list = new ArrayList<>();
        list.add(113L);
        list.add(114L);
        list.add(115L);
        list.add(116L);
        List<Video> videos = videoMapper.selectBatchIds(list);
        List<HomeFeedVO> feedItemVOList = new ArrayList<>();
        for (Video video : videos) {
            HomeFeedVO homeFeedVO = new HomeFeedVO();
            homeFeedVO.setCover(video.getVideoCoverUrl());
            homeFeedVO.setCreateTime(video.getCreateTime());
            homeFeedVO.setTitle(video.getVideoName());
            feedItemVOList.add(homeFeedVO);
        }
        return feedItemVOList;
    }
}
