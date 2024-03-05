package com.damao.controller;

import com.damao.pojo.dto.PublishCommentDTO;
import com.damao.pojo.vo.CommentVO;
import com.damao.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/comment")
@Slf4j
public class CommentController {

    @GetMapping("/videoComments/{vid}")
    public Result<?> getVideoComments(@PathVariable Long vid) {
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

    @PostMapping("/comment")
    public Result<?> publishComment(@RequestBody PublishCommentDTO publishCommentDTO) {

        return Result.success();
    }

    @PostMapping("/reply")
    public Result<?> replyComment(@RequestBody PublishCommentDTO publishCommentDTO) {

        return Result.success();
    }
}
