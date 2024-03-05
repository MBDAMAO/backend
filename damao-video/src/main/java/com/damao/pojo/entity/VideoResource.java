package com.damao.pojo.entity;

import lombok.*;

import com.damao.aspect.BaseEntity;
import java.io.Serializable;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoResource extends BaseEntity implements Serializable {
    Long id;
    Long vid;
    String videoUrl;
}
