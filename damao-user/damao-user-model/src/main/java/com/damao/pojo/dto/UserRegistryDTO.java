package com.damao.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
public class UserRegistryDTO implements Serializable {
    @NotNull(message = "用户名不为空")
    private String username;
    @NotNull(message = "密码不能为空")
    private String password;
    @NotNull(message = "生日不能为空")
    private Date birthday;
    @NotNull(message = "性别不能为空")
    private String sex;
    @NotNull(message = "邮箱不能为空")
    private String email;
}
