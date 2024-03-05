package com.damao.pojo.entity;

import com.damao.aspect.BaseEntity;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoLikesRelation extends BaseEntity {
    private Long id;
    private Long uid;
    private Long vid;
    private Integer status;
}
