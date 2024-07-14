package com.damao.pojo.entity;


import com.damao.aspect.BaseEntity;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity implements Serializable {
    private Long uid;
    private String username;
    private Date birthday;
    private String email;
    private String password;
    private Integer status;
    private String sex;
}
