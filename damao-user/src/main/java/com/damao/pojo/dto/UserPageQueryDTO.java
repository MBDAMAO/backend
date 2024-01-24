package com.damao.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserPageQueryDTO implements Serializable {

    private int page;

    private int pageSize;

    private String username;

    private Long uid;

}
