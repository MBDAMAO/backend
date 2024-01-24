package com.damao.service.impl;

import com.damao.result.PageResult;
import com.damao.mapper.ChatMapper;
import com.damao.pojo.dto.MsgHistoryPageDTO;
import com.damao.pojo.entity.ChatMsg;
import com.damao.service.ChatService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    ChatMapper chatMapper;

    @Override
    public void insert(ChatMsg chatMsg) {
        chatMapper.insert(chatMsg);
    }

    @Override
    public PageResult getHistory(MsgHistoryPageDTO msgHistoryPageDTO) {
        PageHelper.startPage(msgHistoryPageDTO.getPage(),msgHistoryPageDTO.getPageSize());
        Page<ChatMsg> page = chatMapper.getHistory(msgHistoryPageDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }
}
