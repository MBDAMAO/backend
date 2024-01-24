package com.damao.service;

import com.damao.pojo.dto.SendEmailDTO;

public interface EmailService {
    void sendVerifyCode(SendEmailDTO sendEmailDTO);
}
