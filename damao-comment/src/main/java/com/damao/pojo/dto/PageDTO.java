package com.damao.pojo.dto;

import lombok.Data;

import java.util.List;

@Data
public class PageDTO<E> {
    private List<E> data;
    private Integer cursor;
    private Boolean hasMore;
    private Integer count;
}
