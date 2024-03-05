package com.damao.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.damao.aspect.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("comment")
public class Comment extends BaseEntity {
    private Long cid;
    private Long entity_id;
    private String content;
    private Long uid;
    private Long level;
    private Long parentId;
    private Long status;
    private Boolean isDel;
}
