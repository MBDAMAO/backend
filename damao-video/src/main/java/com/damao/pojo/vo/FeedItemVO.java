package com.damao.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeedItemVO {
    private Long feedType;
    private Long feedItemId;
    private Statistics statistics;
    private Author author;
    private Video video;

    @Data
    public static class Author {
        private String nickname;
        private Long followStatus;
        private String uid;
        private String headImg;
    }

    @Data
    public static class Video {
        private Boolean likeStatus;
        private Boolean saveStatus;
        private String src;
        private String cover;
        private String title;
        private LocalDateTime publishTime;
        private String tags;
    }

    @Data
    public static class Statistics {
        private Long likeCount;
        private Long commentCount;
        private Long saveCount;
        private Long shareCount;
        private Long playCount;
    }
}

