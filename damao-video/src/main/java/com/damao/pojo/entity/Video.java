package com.damao.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.damao.aspect.BaseEntity;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("videos")
public class Video extends BaseEntity implements Serializable {
    @TableId
    private Long vid;
    private String videoName;
    private String videoCoverUrl;
    private String videoUrl;
    private Long ownerId;
    private Integer likes;
    private Integer saves;
    private Integer shares;
    @TableField("judge_status")
    private Integer auditStatus;
    @TableField("judge_result")
    private Integer auditResult;
}
