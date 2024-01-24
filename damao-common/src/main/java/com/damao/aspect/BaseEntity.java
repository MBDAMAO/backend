package com.damao.aspect;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseEntity implements Serializable {
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
