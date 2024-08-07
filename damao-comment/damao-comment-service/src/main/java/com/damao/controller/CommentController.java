package com.damao.controller;

import com.damao.pojo.dto.PublishCommentDTO;
import com.damao.pojo.vo.CommentVO;
import com.damao.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 评论接口，包含发布，查看
 */
@RestController
@RequestMapping("/comment")
@Slf4j
public class CommentController {

    @GetMapping("/list/{vid}")
    public Result<?> getVideoComments(@PathVariable Long vid, @RequestParam Integer cursor) {
        CommentVO comment = CommentVO.builder()
                .uid(123L)
                .content("今天吃了狗屎好开心")
                .avatar("1")
                .username("damao")
                .commentId(1134L)
                .likes(154L)
                .build();
        comment.setCreateTime(LocalDateTime.now());
        comment.setUpdateTime(LocalDateTime.now());
        List<CommentVO> r = new ArrayList<>();
        r.add(comment);
        return Result.success(r);
    }

    // 获取某顶级评论子评论
    @GetMapping("/clist/{cid}")
    public Result<?> getNextComments(@PathVariable Long cid) {

        return Result.success();
    }

    // 评论某实体
    @PostMapping("/comment")
    public Result<?> publishComment(@RequestBody PublishCommentDTO publishCommentDTO) {

        return Result.success();
    }

    // 回复某实体下的评论
    @PostMapping("/reply")
    public Result<?> replyComment(@RequestBody PublishCommentDTO publishCommentDTO) {

        return Result.success();
    }
}
