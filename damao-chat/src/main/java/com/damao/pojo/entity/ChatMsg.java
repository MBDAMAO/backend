package com.damao.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

@Data
public class ChatMsg implements Serializable {
    private String fromUid;
    private String toUid;
    private String msg;
    @TableField("send_time")
    private String time;
}
