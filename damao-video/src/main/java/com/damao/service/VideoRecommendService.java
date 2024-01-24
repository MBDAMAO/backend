package com.damao.service;

import com.damao.pojo.entity.Video;

import java.util.List;

public interface VideoRecommendService {
    List<Video> get();

    List<Video> recommendSimilar(Long vid);
}
