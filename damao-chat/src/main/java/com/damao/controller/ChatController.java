package com.damao.controller;

import com.damao.pojo.entity.ChatMsg;
import com.damao.pojo.vo.ChatListItemVO;
import com.damao.result.PageResult;
import com.damao.result.Result;
import com.damao.pojo.dto.MsgHistoryPageDTO;
import com.damao.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/history")
    public Result<List<ChatMsg>> getHistory(@RequestBody MsgHistoryPageDTO msgHistoryPageDTO){
        List<ChatMsg> res = chatService.getHistory(msgHistoryPageDTO);
        return Result.success(res);
    }

    @GetMapping("/list")
    public Result<List<ChatListItemVO>> chatList(){
        List<ChatListItemVO> res = chatService.chatList();
        return Result.success(res);
    }
}
