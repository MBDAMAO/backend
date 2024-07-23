package com.damao.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.damao.context.BaseContext;
import com.damao.mapper.ChatListItemMapper;
import com.damao.pojo.entity.ChatListItem;
import com.damao.pojo.entity.User;
import com.damao.pojo.vo.ChatListItemVO;
import com.damao.result.PageResult;
import com.damao.mapper.ChatMapper;
import com.damao.pojo.dto.MsgHistoryPageDTO;
import com.damao.pojo.entity.ChatMsg;
import com.damao.service.ChatService;
import com.damao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    ChatMapper chatMapper;
    @Autowired
    UserService userService;
    @Autowired
    private ChatListItemMapper chatListItemMapper;

    @Override
    public void insert(ChatMsg chatMsg) {
        chatMapper.insert(chatMsg);
    }

    @Override
    public List<ChatMsg> getHistory(MsgHistoryPageDTO msgHistoryPageDTO) {
        Page<ChatMsg> page = new Page<>(msgHistoryPageDTO.getPage(), msgHistoryPageDTO.getPageSize());
        QueryWrapper<ChatMsg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(true, "from_uid", BaseContext.getCurrentId());
        queryWrapper.eq(true, "to_uid", msgHistoryPageDTO.getToUid());
        IPage<ChatMsg> pageRes = chatMapper.selectPage(page, queryWrapper);
        return pageRes.getRecords();
    }

    @Override
    public List<ChatListItemVO> chatList() {
        Long uid = BaseContext.getCurrentId();
        QueryWrapper<ChatListItem> wrapper = new QueryWrapper<>();
        wrapper.eq(true, "uid", uid);
        wrapper.ge("update_time", System.currentTimeMillis());
        wrapper.last("limit 11");
        List<ChatListItem> chatListItems = chatListItemMapper.selectList(wrapper);
        List<Long> uidList = chatListItems.stream().map(ChatListItem::getFriend).toList();
        List<User> users = userService.batchQueryByIds(uidList);
        List<ChatListItemVO> chatListItemVOS = new ArrayList<>();
        for (User user : users) {
            ChatListItemVO chatListItemVO = new ChatListItemVO();
            chatListItemVO.setId(user.getUid());
            chatListItemVO.setUsername(user.getUsername());
            chatListItemVO.setAvatar(user.getAvatar());
            chatListItemVO.setTips("whatf");
            chatListItemVOS.add(chatListItemVO);
        }
        return chatListItemVOS;
    }
}
