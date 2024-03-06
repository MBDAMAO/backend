package com.damao.pojo.dto;

import lombok.Data;

@Data
public class PublishCommentDTO {
    private String content;
    private Long entityId;
    private Long toUid;
    private Long ipLabel;
}
