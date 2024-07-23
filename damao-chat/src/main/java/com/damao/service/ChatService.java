package com.damao.service;

import com.damao.pojo.vo.ChatListItemVO;
import com.damao.result.PageResult;
import com.damao.pojo.dto.MsgHistoryPageDTO;
import com.damao.pojo.entity.ChatMsg;

import java.util.List;

public interface ChatService {
    void insert(ChatMsg chatMsg);

    List<ChatMsg> getHistory(MsgHistoryPageDTO msgHistoryPageDTO);

    List<ChatListItemVO> chatList();
}
