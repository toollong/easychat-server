package com.easychat.chat.handler;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.VoidAckCallback;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.easychat.chat.dto.ChatInfo;
import com.easychat.chat.dto.FriendInfo;
import com.easychat.chat.dto.VerifyInfo;
import com.easychat.chat.entity.ChatHistory;
import com.easychat.chat.feign.UserFeignService;
import com.easychat.chat.service.ChatService;
import com.easychat.chat.service.UserOnlineService;
import com.easychat.chat.vo.*;
import com.easychat.common.exception.CustomException;
import com.easychat.common.protocol.ResponseResult;
import com.easychat.common.protocol.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: long
 * @Date: 2022-06-14 9:22
 */
@Component
@Slf4j
public class MessageEventHandler {

    @Autowired
    private SocketIOServer socketIOServer;
    @Autowired
    private UserOnlineService userOnlineService;
    @Autowired
    private ChatService chatService;
    @Autowired
    private UserFeignService userFeignService;

    @OnConnect
    public void onConnect(SocketIOClient client) {
        Map<String, List<String>> urlParams = client.getHandshakeData().getUrlParams();
        log.info("客户端：{} 已连接，urlParams:{}", client.getSessionId(), urlParams);
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        Map<String, List<String>> urlParams = client.getHandshakeData().getUrlParams();
        log.info("客户端：{} 断开连接，urlParams:{}", client.getSessionId(), urlParams);
        String userId = client.get("userId");
        if (StringUtils.isNotBlank(userId)) {
            userOnlineService.removeOnlineUser(userId);
            userOnlineService.removeUserAndClient(userId);
        }
        socketIOServer.getBroadcastOperations()
                .sendEvent("onlineUsers", userOnlineService.getOnlineUsers());
        log.info("当前在线人数：{}", userOnlineService.getOnlineCount());
    }

    @OnEvent("online")
    public void onEventOnline(SocketIOClient client, String userId, Integer status) {
        log.info("用户：{} 已上线...", userId);
        client.set("userId", userId);
        userOnlineService.saveUserAndClient(userId, client.getSessionId().toString());
        if (status == 1) {
            userOnlineService.addOnlineUser(userId);
            socketIOServer.getBroadcastOperations()
                    .sendEvent("onlineUsers", userOnlineService.getOnlineUsers());
            log.info("当前在线人数：{}", userOnlineService.getOnlineCount());
        }
    }

    @OnEvent("offline")
    public void onEventOffline(SocketIOClient client, String userId) {
        log.info("用户：{} 已下线...", userId);
        userOnlineService.removeOnlineUser(userId);
        userOnlineService.removeUserAndClient(userId);
        socketIOServer.getBroadcastOperations()
                .sendEvent("onlineUsers", userOnlineService.getOnlineUsers());
        log.info("当前在线人数：{}", userOnlineService.getOnlineCount());
    }

    @OnEvent("changeStatus")
    public void onEventChangeStatus(SocketIOClient client,
                                    String userId, Integer status,
                                    AckRequest ackRequest) {
        log.info("用户：{} 修改状态为 {}", userId, status);
        ChangeStatusRequest request = new ChangeStatusRequest();
        request.setUserId(userId);
        request.setStatus(status);
        ResponseResult<Object> result = userFeignService.changeStatus(request);
        if (result.isSuccess()) {
            if (status == 1) {
                userOnlineService.addOnlineUser(userId);
            }
            if (status == 0) {
                userOnlineService.removeOnlineUser(userId);
            }
            socketIOServer.getBroadcastOperations()
                    .sendEvent("onlineUsers", userOnlineService.getOnlineUsers());
            log.info("当前在线人数：{}", userOnlineService.getOnlineCount());
            if (ackRequest.isAckRequested()) {
                ackRequest.sendAckData("ok");
            }
        }
    }

    @OnEvent("sendVerify")
    public void onEventSendVerify(SocketIOClient client,
                                  AddFriendVerifyRequest request,
                                  AckRequest ackRequest) {
        log.info("用户：{} 发送好友请求：{}", request.getSenderId(), request);
        ResponseResult<Object> addResult = userFeignService.addFriendVerify(request);
        if (addResult.isSuccess()) {
            ResponseResult<VerifyInfo> result = userFeignService
                    .getFriendVerify(request.getSenderId(), request.getReceiverId());
            if (result.isSuccess()) {
                if (ackRequest.isAckRequested()) {
                    ackRequest.sendAckData(result.getData());
                }
                String receiverClientId = userOnlineService.getClientIdByUserId(request.getReceiverId());
                if (StringUtils.isNotBlank(receiverClientId)) {
                    SocketIOClient receiverClient = socketIOServer.getClient(UUID.fromString(receiverClientId));
                    if (receiverClient != null && receiverClient.isChannelOpen()) {
                        receiverClient.sendEvent("receiveVerify", new VoidAckCallback() {
                            @Override
                            protected void onSuccess() {
                                log.info("用户：{} 收到好友请求：{}", request.getReceiverId(), result.getData());
                            }
                        }, result.getData());
                    }
                } else {
                    // 将好友请求保存到redis，等用户上线后发送
                }
            }
        }
    }

    @OnEvent("readVerify")
    public void onEventReadVerify(SocketIOClient client, String userId) {
        log.info("用户：{} 查看新朋友列表...", userId);
        ResponseResult<Object> result = userFeignService.readFriendVerify(userId);
        if (result.isSuccess()) {
            log.info("用户：{} 已读好友请求列表...", userId);
        }
    }

    @OnEvent("rejectApply")
    public void onEventRejectApply(SocketIOClient client, String senderId, String receiverId) {
        log.info("用户：{} 拒绝好友请求...", receiverId);
        EditFriendVerifyRequest request = new EditFriendVerifyRequest();
        request.setSenderId(senderId);
        request.setReceiverId(receiverId);
        request.setStatus(2);
        ResponseResult<String> result = userFeignService.editFriendVerify(request);
        if (result.isSuccess()) {
            String senderClientId = userOnlineService.getClientIdByUserId(senderId);
            if (StringUtils.isNotBlank(senderClientId)) {
                SocketIOClient senderClient = socketIOServer.getClient(UUID.fromString(senderClientId));
                if (senderClient != null && senderClient.isChannelOpen()) {
                    senderClient.sendEvent("applyFailed", senderId, receiverId);
                }
            } else {
                // 将拒绝好友请求保存到redis，等用户上线后发送
            }
        }
    }

    @OnEvent("agreeApply")
    public void onEventAgreeApply(SocketIOClient client, AddFriendRequest request, AckRequest ackRequest) {
        log.info("用户：{} 同意好友请求...", request.getUserId());
        EditFriendVerifyRequest verifyRequest = new EditFriendVerifyRequest();
        verifyRequest.setSenderId(request.getFriendUserId());
        verifyRequest.setReceiverId(request.getUserId());
        verifyRequest.setStatus(1);
        ResponseResult<String> result = userFeignService.editFriendVerify(verifyRequest);
        if (result.isSuccess()) {
            ResponseResult<FriendInfo> senderFriend = userFeignService
                    .getFriendInfo(request.getFriendUserId(), request.getUserId());
            boolean senderAddResult = senderFriend.isSuccess();
            if (!senderAddResult) {
                AddFriendRequest toSender = new AddFriendRequest();
                toSender.setUserId(request.getFriendUserId());
                toSender.setSessionId("0");
                toSender.setSessionTime(request.getCreateTime());
                toSender.setFriendUserId(request.getUserId());
                toSender.setFriendRemark(result.getData());
                toSender.setCreateTime(request.getCreateTime());
                ResponseResult<Object> addFriendResult = userFeignService.addFriend(toSender);
                if (addFriendResult.isSuccess()) {
                    ResponseResult<Object> addSessionResult =
                            chatService.addFriendSession(
                                    request.getFriendUserId(), request.getUserId(), request.getCreateTime());
                    if (addSessionResult.isSuccess()) {
                        senderAddResult = true;
                    }
                }
            }
            ResponseResult<FriendInfo> receiverFriend = userFeignService
                    .getFriendInfo(request.getUserId(), request.getFriendUserId());
            boolean receiverAddResult = receiverFriend.isSuccess();
            if (!receiverAddResult) {
                AddFriendRequest toReceiver = new AddFriendRequest();
                toReceiver.setUserId(request.getUserId());
                toReceiver.setSessionId("0");
                toReceiver.setSessionTime(request.getCreateTime());
                toReceiver.setFriendUserId(request.getFriendUserId());
                toReceiver.setFriendRemark(request.getFriendRemark());
                toReceiver.setCreateTime(request.getCreateTime());
                ResponseResult<Object> addFriendResult = userFeignService.addFriend(toReceiver);
                if (addFriendResult.isSuccess()) {
                    ResponseResult<Object> addSessionResult =
                            chatService.addFriendSession(
                                    request.getUserId(), request.getFriendUserId(), request.getCreateTime());
                    if (addSessionResult.isSuccess()) {
                        receiverAddResult = true;
                    }
                }
            }
            if (senderAddResult && receiverAddResult) {
                ResponseResult<ChatInfo> senderResult = chatService
                        .queryChatInfo(request.getFriendUserId(), request.getUserId());
                ResponseResult<ChatInfo> receiverResult = chatService
                        .queryChatInfo(request.getUserId(), request.getFriendUserId());
                if (senderResult.isSuccess() && receiverResult.isSuccess()) {
                    if (ackRequest.isAckRequested()) {
                        ackRequest.sendAckData(receiverResult.getData());
                    }
                    String receiverClientId = userOnlineService
                            .getClientIdByUserId(request.getFriendUserId());
                    if (StringUtils.isNotBlank(receiverClientId)) {
                        SocketIOClient receiverClient = socketIOServer
                                .getClient(UUID.fromString(receiverClientId));
                        if (receiverClient != null && receiverClient.isChannelOpen()) {
                            receiverClient.sendEvent("applySucceed", senderResult.getData());
                        }
                    } else {
                        // 将同意好友请求保存到redis，等用户上线后发送
                    }
                }
            }
        }
    }

    @OnEvent("removeFriend")
    public void onEventRemoveFriend(SocketIOClient client,
                                    String userId, String friendUserId,
                                    AckRequest ackRequest) {
        log.info("用户：{} 删除好友 {}", userId, friendUserId);
        RemoveFriendRequest request = new RemoveFriendRequest();
        request.setUserId(userId);
        request.setFriendUserId(friendUserId);
        ResponseResult<Object> result = userFeignService.removeFriend(request);
        if (result.isSuccess() && ackRequest.isAckRequested()) {
            ackRequest.sendAckData("ok");
        }
    }

    @OnEvent("resetRemark")
    public void onEventResetRemark(SocketIOClient client,
                                   String userId, String friendUserId, String remark,
                                   AckRequest ackRequest) {
        log.info("用户：{} 修改好友：{} 的备注为：{}", userId, friendUserId, remark);
        ResponseResult<Object> updateResult = chatService.updateFriendRemark(userId, friendUserId, remark);
        if (updateResult.isSuccess()) {
            ResponseResult<FriendInfo> result = userFeignService.getFriendInfo(userId, friendUserId);
            if (result.isSuccess() && ackRequest.isAckRequested()) {
                ackRequest.sendAckData(result.getData());
            }
        }
    }

    @OnEvent("addSession")
    public void onEventAddSession(SocketIOClient client,
                                  String userId, String friendUserId,
                                  AckRequest ackRequest) {
        log.info("用户：{} 添加会话...", userId);
        String sessionTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        ResponseResult<Object> addResult = chatService.addFriendSession(userId, friendUserId, sessionTime);
        if (addResult.isSuccess()) {
            if (addResult.getData() != null && ackRequest.isAckRequested()) {
                ackRequest.sendAckData(addResult.getData());
                return;
            }
            ResponseResult<ChatInfo> result = chatService.queryChatInfo(userId, friendUserId);
            if (result.isSuccess() && ackRequest.isAckRequested()) {
                ackRequest.sendAckData(result.getData());
            }
        }
    }

    @OnEvent("removeSession")
    public void onEventRemoveSession(SocketIOClient client,
                                     String userId, String friendUserId,
                                     AckRequest ackRequest) {
        log.info("用户：{} 删除会话...", userId);
        ResponseResult<Object> result = chatService.removeFriendSession(userId, friendUserId);
        if (result.isSuccess() && ackRequest.isAckRequested()) {
            ackRequest.sendAckData("ok");
        }
    }

    //    @OnEvent("joinRoom")
    //    public void onEventJoinRoom(SocketIOClient client, String sessionId) {
    //        log.info("客户端：{} 加入房间 {}", client.getSessionId(), sessionId);
    //        client.joinRoom(sessionId);
    //        log.info("房间：{} 的成员数量：{}", sessionId,
    //                socketIOServer.getRoomOperations(sessionId).getClients().size());
    //    }

    @OnEvent("sendMsg")
    public void onEventSendMsg(SocketIOClient client, ChatHistory message, AckRequest ackRequest) {
        log.info("用户：{} 发送消息：{}", message.getSenderId(), message);
        if (message.getType() == 0) {
            try {
                ResponseResult<List<ChatHistory>> result = chatService.createChatHistory(message, null);
                if (result.isSuccess()) {
                    String receiverClientId = userOnlineService.getClientIdByUserId(message.getReceiverId());
                    if (StringUtils.isNotBlank(receiverClientId)) {
                        if (ackRequest.isAckRequested()) {
                            ackRequest.sendAckData(result.getData().get(0), "ok");
                        }
                        SocketIOClient receiverClient = socketIOServer.getClient(UUID.fromString(receiverClientId));
                        if (receiverClient != null && receiverClient.isChannelOpen()) {
                            receiverClient.sendEvent("receiveMsg", new VoidAckCallback() {
                                @Override
                                protected void onSuccess() {
                                    log.info("用户：{} 收到消息：{}", message.getReceiverId(), result.getData().get(0));
                                }
                            }, result.getData().get(0));
                        }
                    } else {
                        if (ackRequest.isAckRequested()) {
                            ackRequest.sendAckData(result.getData().get(0), "offline");
                            log.info("用户：{} 不在线...", message.getReceiverId());
                        }
                        // 将该消息保存为离线消息 redis
                    }
                }
            } catch (CustomException e) {
                ResultCode resultCode = e.getResultCode();
                if (resultCode.code() == 20002) {
                    if (ackRequest.isAckRequested()) {
                        ackRequest.sendAckData(null, "notFriend");
                    }
                }
            }
        }
        if (message.getType() == 1 || message.getType() == 2) {
            String receiverClientId = userOnlineService.getClientIdByUserId(message.getReceiverId());
            if (StringUtils.isNotBlank(receiverClientId)) {
                if (ackRequest.isAckRequested()) {
                    ackRequest.sendAckData(message, "ok");
                }
                SocketIOClient receiverClient = socketIOServer.getClient(UUID.fromString(receiverClientId));
                if (receiverClient != null && receiverClient.isChannelOpen()) {
                    receiverClient.sendEvent("receiveMsg", new VoidAckCallback() {
                        @Override
                        protected void onSuccess() {
                            log.info("用户：{} 收到消息：{}", message.getReceiverId(), message);
                        }
                    }, message);
                }
            } else {
                if (ackRequest.isAckRequested()) {
                    ackRequest.sendAckData(message, "offline");
                    log.info("用户：{} 不在线...", message.getReceiverId());
                }
                // 将该消息保存为离线消息 redis
            }
        }
    }

    @OnEvent("readMessages")
    public void onEventReadMessages(SocketIOClient client, String sessionId, String userId) {
        log.info("用户：{} 打开会话：{}", userId, sessionId);
        ResponseResult<Object> result = chatService.readChatHistory(sessionId, userId);
        if (result.isSuccess()) {
            log.info("用户：{} 已读会话消息...", userId);
        }
    }
}
