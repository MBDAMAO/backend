package com.damao.pojo.vo;

import lombok.Data;

@Data
public class FeedItemVO {
    private Long feedType;
    private Long feedItemId;
    private Object author;
    private Object video;
    private Object statistics;

    private static class Author {
        private String nickname;

    }
}

