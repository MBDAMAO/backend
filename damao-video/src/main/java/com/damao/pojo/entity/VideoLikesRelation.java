package com.damao.pojo.entity;

import com.damao.context.BaseContext;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoLikesRelation extends BaseContext {
    private Long id;
    private Long uid;
    private Long vid;
    private Integer status;
}
