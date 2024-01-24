package com.damao.service.impl;

import com.damao.constant.RedisNameConstant;
import com.damao.context.BaseContext;
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

@Service
public class VideoRecommendServiceImpl implements VideoRecommendService {

    @Autowired
    VideoMapper videoMapper;

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
}
