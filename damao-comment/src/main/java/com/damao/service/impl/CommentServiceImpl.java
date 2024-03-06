package com.damao.service.impl;

import com.damao.pojo.dto.PageDTO;
import com.damao.pojo.dto.PublishCommentDTO;
import com.damao.pojo.entity.Comment;
import com.damao.service.CommentService;
import org.springframework.stereotype.Service;


@Service
public class CommentServiceImpl implements CommentService {

    @Override
    public PageDTO<Comment> getCommentsByEntity(Long entityId) {
        // 如果是自己的评论
            // 已删除->不显示
            // 未审核结束->也显示
        // 他人评论
            // 未审核结束->不显示
        return null;
    }

    @Override
    public PageDTO<Comment> getCommentsByParent(Long parentId) {
        return null;
    }

    @Override
    public void comment(PublishCommentDTO publishCommentDTO) {
        // 写DB
        // 写缓存
        // 记录行为
        // 风控Event
        // 通知Event
    }

    @Override
    public void reply(PublishCommentDTO publishCommentDTO) {
        // 写DB
        // 写缓存
        // 记录行为
        // 风控Event
        // 通知Event
    }


}
