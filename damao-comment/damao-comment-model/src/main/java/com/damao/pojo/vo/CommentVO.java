package com.damao.pojo.vo;

import com.damao.aspect.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = false)
@Data
@Builder
public class CommentVO extends BaseEntity {
    private Long commentId;
    private String content;
    private String avatar;
    private Long likes;
    private String username;
    private Long uid;
}
