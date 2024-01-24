package com.damao.service.impl;

import com.damao.mapper.FollowMapper;
import com.damao.pojo.entity.FollowerRelation;
import com.damao.service.FollowerRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowerRelationServiceImpl implements FollowerRelationService {

    @Autowired
    private FollowMapper followMapper;

    @Override
    public List<Long> getFollowersById(Long id) {
        return followMapper.getFollowersById(id);
    }

    @Override
    public List<Long> getFollowedById(Long id) {
        return followMapper.getFollowedById(id);
    }

    @Override
    public void add(Long id, Long currentId) {
        followMapper.add(id,currentId);
    }

    @Override
    public FollowerRelation getStatus(Long currentId, Long id) {
        FollowerRelation followerRelation = followMapper.getStatus(currentId,id);
        return followerRelation;
    }

    @Override
    public void remove(Long id, Long currentId) {
        followMapper.remove(id,currentId);
    }
}
