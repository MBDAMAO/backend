package com.damao.pojo.entity;


import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Video extends BaseEntity implements Serializable {
    private Long vid;
    private String videoName;
    private String videoCoverUrl;
    private String videoUrl;
    private Long ownerId;
    private Integer likes;
    private Integer saves;
    private Integer shares;
    private Integer auditStatus;
    private Integer auditResult;
}
