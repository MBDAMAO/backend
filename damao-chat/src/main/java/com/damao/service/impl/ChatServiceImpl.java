package com.damao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.damao.result.PageResult;
import com.damao.mapper.ChatMapper;
import com.damao.pojo.dto.MsgHistoryPageDTO;
import com.damao.pojo.entity.ChatMsg;
import com.damao.service.ChatService;
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
        Page<ChatMsg> page = new Page<>(msgHistoryPageDTO.getPage(), msgHistoryPageDTO.getPageSize());
        QueryWrapper<ChatMsg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(true, "from_uid",msgHistoryPageDTO.getFromUid());
        queryWrapper.eq(true, "to_uid",msgHistoryPageDTO.getToUid());
        IPage<ChatMsg> pageRes = chatMapper.selectPage(page, queryWrapper);
        return new PageResult(pageRes.getTotal(), pageRes.getRecords());
    }
}
