package com.mei.handler;

import com.mei.Main;
import com.mei.utils.FileUtils;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.io.IOException;

import static com.mei.utils.BroadcastUtils.broadcastHeartCheck;
import static com.mei.utils.BroadcastUtils.broadcastMessage;

@WebSocket
public class ChatWebSocketHandler {

    //确认发送者的字符串，用UUID生成的
    private String sender = "6e28cc9715cd4cb09c3d8c0789fe361f";

    //心跳检测的字符串，也是UUID生成的
    private String heart = "790c1e4f11134c73b8f1c1c11b448f83";

    //ws 连接成功
    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        String username = "User" + Main.nextUserNumber++;
        Main.userUsernameMap.put(user, username);
        broadcastMessage("Server", username + " joined the chat");
    }

    //ws 连接关闭
    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        String username = Main.userUsernameMap.get(user);
        Main.userUsernameMap.remove(user);
        broadcastMessage("Server", username + " left the chat");

        //清理数据
        FileUtils.deleteAll("upload");
    }

    /**
     * ws 接收前端的 Message 并广播出去
     *
     * @param user
     * @param message 前端的发来的message
     */
    @OnWebSocketMessage
    public void onMessage(Session user, String message) throws IOException {
        if (message.equals(sender)) {
            Main.tempSender = user;
        } else if (message.equals(heart)) {
            broadcastHeartCheck();
        } else {
            // 广播字符串
            broadcastMessage(Main.userUsernameMap.get(user), message);
        }
    }

}
