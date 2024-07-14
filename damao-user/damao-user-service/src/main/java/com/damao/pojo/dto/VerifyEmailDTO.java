package com.damao.pojo.dto;

import lombok.Data;

@Data
public class VerifyEmailDTO {
    private String email;
    private String code;
}
