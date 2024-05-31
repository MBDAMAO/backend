package com.damao.service;

import com.damao.pojo.dto.PageDTO;
import com.damao.pojo.dto.PublishCommentDTO;
import com.damao.pojo.entity.Comment;


public interface CommentService {
    PageDTO<?> getCommentsByEntity(Long entityId, Integer cursor);
    PageDTO<Comment> getCommentsByParent(Long parentId);
    void comment(PublishCommentDTO publishCommentDTO);
    void reply(PublishCommentDTO publishCommentDTO);
}
