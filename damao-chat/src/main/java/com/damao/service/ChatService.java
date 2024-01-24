package com.damao.service;

import com.damao.result.PageResult;
import com.damao.pojo.dto.MsgHistoryPageDTO;
import com.damao.pojo.entity.ChatMsg;

public interface ChatService {
    void insert(ChatMsg chatMsg);

    PageResult getHistory(MsgHistoryPageDTO msgHistoryPageDTO);
}
