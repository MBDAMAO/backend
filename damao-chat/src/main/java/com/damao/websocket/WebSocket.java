package com.damao.websocket;

import com.alibaba.fastjson.JSON;
import com.damao.pojo.dto.ChatMsgDTO;
import com.damao.pojo.entity.ChatMsg;
import com.damao.service.ChatService;
import com.damao.service.impl.ChatServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@Slf4j
@ServerEndpoint("/whisper/{uid}")
public class WebSocket implements BeanFactoryAware , InitializingBean {

    private static ChatService chatService;

    private static BeanFactory beanfactory;

    private Session session;
    private Long uid;

    // 用来记录Bean，虽然默认单例，但websocket连接是特殊的
    private static final CopyOnWriteArraySet<WebSocket> webSockets = new CopyOnWriteArraySet<>();
    // 用来存在线连接用户信息
    private static final ConcurrentHashMap<Long, Session> sessionPool = new ConcurrentHashMap<>();

    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
        beanfactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() {
        chatService = beanfactory.getBean(ChatServiceImpl.class);
    }


    @OnOpen
    public void onOpen(Session session, @PathParam(value = "uid") Long uid) {
        try {
            this.session = session;
            this.uid = uid;
            webSockets.add(this);
            sessionPool.put(uid, session);
            log.info("[websocket消息] 有新的连接，总数为:" + webSockets.size());
        } catch (RuntimeException e) {
            log.info("{}", e.getMessage());
        }
    }

    @OnClose
    public void onClose() {
        try {
            webSockets.remove(this);
            sessionPool.remove(uid);
            log.info("[websocket消息] 有连接断开，总数为:" + webSockets.size());
        } catch (RuntimeException e) {
            log.info("{}", e.getMessage());
        }
    }

    @OnMessage
    public void onMessage(String message) {
        ChatMsgDTO chatMsgDTO = JSON.parseObject(message, ChatMsgDTO.class);
        String msg = chatMsgDTO.getMsg();
        chatMsgDTO.setTime(LocalDateTime.now());
        chatMsgDTO.setFromUid(uid);
        Long[] toUid = chatMsgDTO.getToUid();
        ChatMsg chatMsg = new ChatMsg();
        BeanUtils.copyProperties(chatMsgDTO,chatMsg);
        chatMsg.setToUid(chatMsgDTO.getToUid()[0]);
        chatMsgDTO.setToUid(null);
        if (toUid.length == 1) {
            chatService.insert(chatMsg);
            sendOneMessage(toUid[0], chatMsgDTO.toString());
        } else {
            sendMoreMessage(toUid, chatMsgDTO.toString());
        }
        log.info("[websocket消息] 收到客户端消息:" + msg);
    }

    /**
     * 发送错误时的处理
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误,原因:" + error.getMessage());
        error.printStackTrace();
    }

    // 此为广播消息
    public void sendAllMessage(String message) {
        log.info("[websocket消息] 广播消息:" + message);
        for (WebSocket webSocket : webSockets) {
            try {
                if (webSocket.session.isOpen()) {
                    // 异步发送消息
                    webSocket.session.getAsyncRemote().sendText(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 此为单点消息
    public void sendOneMessage(Long userId, String message) {
        Session session = sessionPool.get(userId);
        if (session != null && session.isOpen()) {
            try {
                log.info("[websocket消息] 单点消息:" + message);
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 此为单点消息(多人)
    public void sendMoreMessage(Long[] userIds, String message) {
        for (Long userId : userIds) {
            Session session = sessionPool.get(userId);
            if (session != null && session.isOpen()) {
                try {
                    log.info("[websocket消息] 单点消息:" + message);
                    session.getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
