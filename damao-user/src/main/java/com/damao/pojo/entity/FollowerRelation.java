package com.damao.pojo.entity;

import lombok.*;
import com.damao.aspect.BaseEntity;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowerRelation extends BaseEntity implements Serializable {
    private Long fans_user_id;
    private Long uid;
    private Long followerId;
}
