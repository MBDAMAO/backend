package com.damao.mapper;

import com.damao.annotation.AutoFill;
import com.damao.enumeration.OperationType;
import com.damao.pojo.entity.VideoResource;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface VideoResourceMapper {

    @Select("select video_url from video_platform.video_resource where vid = #{vid}")
    String getUrlByVid(Long vid);

    @Insert("insert into video_platform.video_resource(vid, video_url, create_time, update_time)" +
            " VALUES (#{vid},#{videoUrl},#{createTime},#{updateTime})")
    @AutoFill(OperationType.INSERT)
    void save(VideoResource videoResource);

    @Select("SELECT category_id from video_platform.video_category_relation where vid=#{vid}")
    Integer getTypeIdByVid(Long vid);
}
