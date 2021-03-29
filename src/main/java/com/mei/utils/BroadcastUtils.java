package com.mei.utils;

import com.mei.entity.Message;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;

import static com.mei.Main.userUsernameMap;
import static com.mei.utils.HtmlUtils.*;

public class BroadcastUtils {

    //Sends a Message from one user to all users, along with a list of current usernames
    public static void broadcastMessage(String sender, String message) {
        for (Session session : userUsernameMap.keySet()) {
            try {
                //给前端发消息
                session.getRemote().sendString(
                        //生成json，这个json就是前端收到的msg.data
                        String.valueOf(new JSONObject()
                                .put("userMessage", createHtmlMessageFromSender(sender, message))//userMessage是一个HTML标签
                                .put("userlist", userUsernameMap.values())
                        ));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //心跳检测
    public static void broadcastHeartCheck() {
        for (Session session : userUsernameMap.keySet()) {
            try {
                //给前端发消息
                session.getRemote().sendString(
                        String.valueOf(new JSONObject()
                                .put("heartCheck", "heartCheck:" + UUID.randomUUID().toString()))
                        );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //广播文件
    public static void broadcastFile(Message message) {
        for (Session session : userUsernameMap.keySet()) {
            try {
                //给前端发送消息
                session.getRemote().sendString(
                        String.valueOf(new JSONObject()
                                .put("userMessage", createHtml(message))
                                .put("userlist", userUsernameMap.values())
                        ));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
