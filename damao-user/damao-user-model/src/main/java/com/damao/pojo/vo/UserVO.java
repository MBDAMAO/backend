package com.damao.pojo.vo;


import com.damao.pojo.entity.BaseEntity;
import lombok.*;

import java.io.Serializable;
import java.util.Date;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVO extends BaseEntity implements Serializable {
    private Long uid;
    private String username;
    private Date birthday;
    private String email;
    private String password;
    private Integer status;
    private String sex;
    private String avatar;
    /**
     *  用以判断是否互相关注
     */
    private Boolean followedTogether;
}
