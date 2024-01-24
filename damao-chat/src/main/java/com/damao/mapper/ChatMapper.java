package com.damao.mapper;

import com.damao.pojo.dto.MsgHistoryPageDTO;
import com.damao.pojo.entity.ChatMsg;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMapper {

    @Insert("insert into video_platform.chat_msg(from_uid, to_uid, send_time,msg) " +
            "VALUES (#{fromUid},#{toUid},#{time},#{msg})")
    void insert(ChatMsg chatMsg);

    Page<ChatMsg> getHistory(MsgHistoryPageDTO msgHistoryPageDTO);
}
