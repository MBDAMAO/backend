package com.damao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.damao.annotation.AutoFill;
import com.damao.enumeration.OperationType;
import com.damao.pojo.entity.Video;
import com.damao.pojo.entity.VideoLikesRelation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface VideoMapper extends BaseMapper<Video> {

    @AutoFill(OperationType.INSERT)
    void save(Video video);

    @AutoFill(OperationType.UPDATE)
    void update(Video video);

    //TODO 字段名称映射关系调整
    @Select("select *  from video_platform.videos where vid=#{vid}")
    Video getVideoById(Long vid);

    //这里先不加时间
    void updateLikeRelation(VideoLikesRelation videoLikesRelation);

    @Select("select * from video_platform.video_likes_relation where uid = #{uid} and vid = #{vid}")
    VideoLikesRelation getLikeRelation(Long uid, Long vid);

    @Insert("insert into video_platform.video_likes_relation(uid, vid,status) values (#{uid},#{vid},#{status})")
    void saveLikeRelation(VideoLikesRelation videoLikesRelation);

    @Select("SELECT * FROM video_platform.videos ORDER BY RAND() limit 4")
    List<Video> getRandFour();

    List<Video> getByIds(List<Long> vidList);

    @Select("SELECT like_vector from video_platform.user_model where uid=#{uid}")
    String getModel(Long uid);
}
