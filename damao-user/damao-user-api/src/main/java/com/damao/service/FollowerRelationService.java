package com.damao.service;

import com.damao.pojo.entity.FollowerRelation;

import java.util.List;

public interface FollowerRelationService {
    List<Long> getFollowersById(Long id);
    List<Long> getFollowedById(Long id);

    void remove(Long id, Long currentId);

    void add(Long id, Long currentId);

    FollowerRelation  getStatus(Long currentId, Long id);
}
