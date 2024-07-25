package com.damao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.damao.pojo.entity.ChatMsg;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ChatMapper extends BaseMapper<ChatMsg> {

//    @Insert("insert into video_platform.chat_msg(from_uid, to_uid, send_time,msg) " +
//            "VALUES (#{fromUid},#{toUid},#{time},#{msg})")
//    int insert(ChatMsg chatMsg);
}
