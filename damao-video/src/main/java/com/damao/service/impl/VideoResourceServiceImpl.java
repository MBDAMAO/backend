package com.damao.service.impl;

import com.damao.constant.RedisNameConstant;
import com.damao.context.BaseContext;
import com.damao.mapper.VideoResourceMapper;
import com.damao.service.VideoResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class VideoResourceServiceImpl implements VideoResourceService {
    @Autowired
    VideoResourceMapper videoResourceMapper;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;


    @Override
    public String getUrlByVid(Long vid) {
        // 这里要对用户模型做一个更新 TODO 实现异步更新user_model
        Long uid = BaseContext.getCurrentId();
        Long[] userVector = (Long[]) ((Map<?, ?>) redisTemplate.opsForHash().get(RedisNameConstant.USER_MODEL, String.valueOf(uid))).get("model");

        Integer typeId = videoResourceMapper.getTypeIdByVid(vid);
        assert userVector != null;
        userVector[typeId - 1] += 1;
        Map<String, Long[]> userModelMap = new HashMap<>();
        userModelMap.put("model", userVector);
        redisTemplate.opsForHash().put(RedisNameConstant.USER_MODEL, String.valueOf(uid), userModelMap);

        return videoResourceMapper.getUrlByVid(vid);
    }
}
