package com.damao.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.damao.aspect.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("comment")
public class Comment extends BaseEntity {
    @TableId
    private Long cid;

    @TableField("entity_id")
    private Long entityId;
    private String content;
    private Long uid;
    private Long level;
    @TableField("ip_label")
    private String ipLabel;
    @TableField("parent_id")
    private Long parentId;
    private Long status;
    private Boolean isDel;
}
