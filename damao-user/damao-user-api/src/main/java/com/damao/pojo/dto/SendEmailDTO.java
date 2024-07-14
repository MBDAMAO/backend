package com.damao.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SendEmailDTO implements Serializable {
    private String email;
    private Long uid;
}
