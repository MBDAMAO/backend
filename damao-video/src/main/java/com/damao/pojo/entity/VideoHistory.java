package com.damao.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("video_history")
public class VideoHistory extends BaseEntity {
    @TableId
    private Long id;
    private Long uid;
    private Long vid;
    private Integer status;
}
