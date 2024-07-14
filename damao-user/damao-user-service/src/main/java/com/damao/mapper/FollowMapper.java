package com.damao.mapper;

import com.damao.pojo.entity.FollowerRelation;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FollowMapper {
    List<Long> getFollowersById(Long id);
    List<Long> getFollowedById(Long id);

    @Insert("insert into video_platform.follower_relation (uid, follower_id, create_time, update_time)" +
            " values (#{id},#{currentId},time(NOW()),time(NOW()))")
    void add(Long id, Long currentId);

    @Delete("delete from video_platform.follower_relation where uid = #{id} and follower_id = #{currentId}")
    void remove(Long id, Long currentId);

    @Select("select * from video_platform.follower_relation where uid=#{id} and follower_id=#{currentId}")
    FollowerRelation getStatus(Long currentId, Long id);
}
