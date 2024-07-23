package com.damao.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.damao.aspect.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("chat_list")
public class ChatListItem extends BaseEntity {
    @TableId
    private Long id;
    private Long uid;
    private Long friend;
}
