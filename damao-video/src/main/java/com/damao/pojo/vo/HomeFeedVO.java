package com.damao.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HomeFeedVO {
    private String cover;
    private String title;
    private Long ownerId;
    private LocalDateTime createTime;
}
