package com.damao.service;

import com.damao.pojo.entity.Video;
import com.damao.pojo.vo.FeedItemVO;
import com.damao.pojo.vo.HomeFeedVO;

import java.util.List;

public interface VideoRecommendService {
    List<Video> get();

    List<Video> recommendSimilar(Long vid);

    List<FeedItemVO> feed();

    List<HomeFeedVO> homeFeed();
}
