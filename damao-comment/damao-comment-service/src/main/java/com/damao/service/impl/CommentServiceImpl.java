package com.damao.service.impl;

import com.alibaba.fastjson.JSON;
import com.damao.context.BaseContext;
import com.damao.context.IpContext;
import com.damao.feign.UserService;
import com.damao.mapper.CommentMapper;
import com.damao.pojo.dto.PageDTO;
import com.damao.pojo.dto.PublishCommentDTO;
import com.damao.pojo.entity.Comment;
import com.damao.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Autowired
    RedisTemplate<Long, Comment> redisTemplate2;

    @Autowired
    UserService userService;

    // @Autowired
    // CommentStatisticsService statisticsService;

    @Override
    public PageDTO<?> getCommentsByEntity(Long entityId, Integer cursor) {
        // 查出20个数据
        // 查缓存索引表（每组缓存300楼）
        Set<String> commentIdx = Objects.requireNonNull(redisTemplate.opsForZSet()
                .rangeByScore("reply_index:" + entityId, cursor, 20));
        if (commentIdx.isEmpty()) {
            return null;
        }
        List<String> commentNotFull = redisTemplate.opsForValue().multiGet(commentIdx);
        if (commentNotFull == null) {
            return null;
        }
        // 已经在缓存的从idx中拿走
        for (int i = 0; i < commentIdx.size(); i++) {
            if (commentNotFull.get(i) != null) {
                commentIdx.remove(String.valueOf(JSON.parseObject(commentNotFull.get(i),Comment.class).getCid()));
            }
        }
        // 不存在缓存的实体加入缓存
        List<Comment> comments = commentMapper.selectBatchIds(commentIdx);
        redisTemplate2.opsForValue().multiSet(comments.stream().collect(
                Collectors.toMap(Comment::getCid,comment -> comment,
                        (existing, replacement) -> existing
                        )
        ));
        // 旧方案
        // List<Comment> comments = commentMapper.queryByEntity(entityId, cursor);

        comments.removeIf(comment -> comment.getStatus() == 0
                && !comment.getUid().equals(BaseContext.getCurrentId())
                && !comment.getIsDel()
        );
        if (comments.size() == 0) {
            return null;
        }

        PageDTO<Object> pageDTO = new PageDTO<>();
        pageDTO.setCount(comments.size());
        pageDTO.setCursor(cursor + comments.size());
        // 组装未完成
        // pageDTO.setData(comments);
        pageDTO.setHasMore(comments.size() < 20);
        // TODO 这里判断的逻辑可以改进

        // 根据每个comment里的uid查询用户个人信息
        List<Long> uidList = comments.stream().map(Comment::getUid).collect(Collectors.toList());
        List<?> userInfoList = userService.query(uidList);

        // 根据每个commentIds获取评论统计信息
        List<Long> cidList = comments.stream().map(Comment::getCid).collect(Collectors.toList());
        // List<?> statistics = statisticsService.query(cidList);

        List<Object> result = new ArrayList<>();
        // 直接使用弱类型了，组装数据
        for (int i = 0; i < comments.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("user", userInfoList.get(i));
            map.put("comment", comments.get(i));
            // map.put("statistics", statistics.get((i)));
            result.add(map);
        }
        pageDTO.setData(result);
        return pageDTO;
    }

    @Override
    public PageDTO<Comment> getCommentsByParent(Long parentId) {

        return null;
    }

    @Override
    public void comment(PublishCommentDTO publishCommentDTO) {
        // 写DB
        Comment comment = new Comment();
        comment.setContent(publishCommentDTO.getContent());
        comment.setEntityId(publishCommentDTO.getEntityId());
        comment.setUid(BaseContext.getCurrentId());
        comment.setIpLabel(IpContext.getIp());
        // comment.setParentId(publishCommentDTO.getToUid());
        commentMapper.insert(comment);
        // 写缓存
        // redisTemplate.opsForValue().set();
        // 计数服务
        // 记录行为
        // 风控Event
        // 通知Event
    }

    @Override
    public void reply(PublishCommentDTO publishCommentDTO) {
        // 写DB
        Comment comment = new Comment();
        comment.setContent(publishCommentDTO.getContent());
        comment.setEntityId(publishCommentDTO.getEntityId());
        comment.setUid(BaseContext.getCurrentId());
        comment.setIpLabel(IpContext.getIp());
        comment.setParentId(publishCommentDTO.getToUid());
        commentMapper.insert(comment);
        // 写缓存
        // 记录行为
        // 风控Event
        // 通知Event
    }

}
