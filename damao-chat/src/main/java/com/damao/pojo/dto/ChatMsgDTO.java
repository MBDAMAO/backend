package com.damao.pojo.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ChatMsgDTO implements Serializable {
    private Long[] toUid;
    private String msg;
    private String time;
}
