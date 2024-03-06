package com.damao.service;

import com.damao.pojo.dto.PageDTO;
import com.damao.pojo.dto.PublishCommentDTO;
import com.damao.pojo.entity.Comment;


public interface CommentService {
    PageDTO<Comment> getCommentsByEntity(Long entityId);
    PageDTO<Comment> getCommentsByParent(Long parentId);
    void comment(PublishCommentDTO publishCommentDTO);
    void reply(PublishCommentDTO publishCommentDTO);
}
