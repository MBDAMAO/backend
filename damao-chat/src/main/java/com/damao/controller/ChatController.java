package com.damao.controller;

import com.damao.result.PageResult;
import com.damao.result.Result;
import com.damao.pojo.dto.MsgHistoryPageDTO;
import com.damao.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/history")
    public Result<PageResult> getHistory(MsgHistoryPageDTO msgHistoryPageDTO){
        PageResult pageResult = chatService.getHistory(msgHistoryPageDTO);
        return Result.success(pageResult);
    }


}
