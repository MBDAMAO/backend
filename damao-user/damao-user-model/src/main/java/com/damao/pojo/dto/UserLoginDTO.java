package com.damao.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class UserLoginDTO implements Serializable {
    @NotNull(message = "用户名不为空")
    private String username;
    @NotNull(message = "密码不能为空")
    private String password;
}
