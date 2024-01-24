package com.damao.service;

import com.damao.pojo.entity.Video;
import com.damao.pojo.entity.VideoLikesRelation;
import com.damao.pojo.vo.VideoRankVO;

import java.util.List;

public interface VideoService {

    Long publish(Video video);

    void update(Video video);

    Video getVideoById(Long vid);

    List<VideoRankVO> getDailyTopTenViewVideos();

    VideoLikesRelation getLikeRelation(Long uid, Long vid);

    void saveLikeRelation(VideoLikesRelation videoLikesRelation);

    void updateLikeRelation(VideoLikesRelation videoLikesRelation);

    Integer getVideoLikesCount(Long vid);

    void saveLikeRelationUser(Long vid, Integer status);

    Integer getLikesStatus(Long vid);
}
