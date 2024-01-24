package com.damao.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoRankVO {
    private Long vid;
    private String videoName;
    private String videoCoverUrl;
    private String videoUrl;
    private Long ownerId;
    private Integer likes;
    private Integer saves;
    private Integer shares;
    private Double views;
    private LocalDateTime updateTime;
}
