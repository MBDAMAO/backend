package com.damao.pojo.dto;

import lombok.Data;

@Data
public class PublishCommentDTO {
    private String context;
    private Long vid;
    private Long toUid;
}
