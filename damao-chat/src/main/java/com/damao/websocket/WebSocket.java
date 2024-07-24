package com.damao.websocket;

import com.alibaba.fastjson.JSON;
import com.damao.pojo.dto.ChatMsgDTO;
import com.damao.pojo.entity.ChatMsg;
import com.damao.properties.JwtProperties;
import com.damao.service.ChatService;
import com.damao.service.impl.ChatServiceImpl;
import com.damao.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@Slf4j
@ServerEndpoint("/whisper/{token}")
public class WebSocket implements BeanFactoryAware, InitializingBean {

    private static ChatService chatService;

    private static BeanFactory beanfactory;

    private Session session;
    private String uid;

    private static JwtProperties jwtProperties;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 用来记录Bean，虽然默认单例，但websocket连接是特殊的
    private static final CopyOnWriteArraySet<WebSocket> webSockets = new CopyOnWriteArraySet<>();
    // 用来存在线连接用户信息
    private static final ConcurrentHashMap<String, Session> sessionPool = new ConcurrentHashMap<>();

    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
        beanfactory = beanFactory;
    }

    /*
     * 不能用Autowired
     */
    @Override
    public void afterPropertiesSet() {
        chatService = beanfactory.getBean(ChatServiceImpl.class);
        jwtProperties = beanfactory.getBean(JwtProperties.class);
    }


    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        String uid = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token).get("user_id").toString();
        this.session = session;
        this.uid = uid;
        webSockets.add(this);
        sessionPool.put(uid, session);
        log.info("[websocket消息] 有新的连接uid:{}，总数为:{}",uid , webSockets.size());
    }

    @OnClose
    public void onClose() {
        try {
            webSockets.remove(this);
            if (uid != null) {
                sessionPool.remove(uid);
            }
            log.info("[websocket消息] 有连接断开，总数为:{}", webSockets.size());
        } catch (RuntimeException e) {
            log.error("{}", e.getMessage());
        }
    }

    @OnMessage
    public void onMessage(String message) {
        Map<String, Object> chatMsgDTO = JSON.parseObject(message, HashMap.class);
        String msg = (String) chatMsgDTO.get("msg");
        chatMsgDTO.put("time", LocalDateTime.now().format(formatter));
        chatMsgDTO.put("fromUid", uid);
        List<String> toUid = (List<String>) chatMsgDTO.get("toUid");
        ChatMsg chatMsg = new ChatMsg();
        ChatMsg chatMsgReverse = new ChatMsg();
        String localDateTime = LocalDateTime.now().format(formatter);

        chatMsgReverse.setToUid(uid);
        chatMsgReverse.setMsg(msg);
        chatMsgReverse.setFromUid(toUid.get(0));
        chatMsgReverse.setTime(localDateTime);
        chatMsgReverse.setIsMyMsg(false);

        chatMsg.setFromUid(uid);
        chatMsg.setMsg(msg);
        chatMsg.setToUid(toUid.get(0));
        chatMsg.setTime(localDateTime);
        chatMsg.setIsMyMsg(true);


        if (toUid.size() == 1) {
            chatService.insert(chatMsgReverse);
            chatService.insert(chatMsg);
            sendOneMessage(toUid.get(0), JSON.toJSONString(chatMsgDTO));
        } else {
            sendMoreMessage(toUid, JSON.toJSONString(chatMsgDTO));
        }
        log.info("[websocket消息] 收到客户端消息:{}", msg);
    }

    /**
     * 发送错误时的处理
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误,原因:{}", error.getMessage());
    }

    // 此为广播消息
    public void sendAllMessage(String message) {
        log.info("[websocket消息] 广播消息:{}", message);
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
    public void sendOneMessage(String userId, String message) {
        Session session = sessionPool.get(userId);
        if (session != null && session.isOpen()) {
            try {
                log.info("[websocket消息] 单点消息:{}", message);
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 此为单点消息(多人)
    public void sendMoreMessage(List<String> userIds, String message) {
        for (String userId : userIds) {
            Session session = sessionPool.get(userId);
            if (session != null && session.isOpen()) {
                try {
                    log.info("[websocket消息] 单点消息:{}", message);
                    session.getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
