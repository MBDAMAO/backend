package com.damao.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MsgHistoryPageDTO implements Serializable {
    private int page;

    private int pageSize;

    private Long fromUid;

    private Long toUid;
}
