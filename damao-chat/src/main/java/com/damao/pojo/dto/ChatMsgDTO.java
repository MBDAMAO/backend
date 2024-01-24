package com.damao.pojo.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ChatMsgDTO implements Serializable {
    private Long fromUid;
    private Long[] toUid;
    private String msg;
    private LocalDateTime time;
}
