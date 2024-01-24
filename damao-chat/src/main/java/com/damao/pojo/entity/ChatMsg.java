package com.damao.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ChatMsg implements Serializable {
    private Long fromUid;
    private Long toUid;
    private String msg;
    private LocalDateTime time;
}
