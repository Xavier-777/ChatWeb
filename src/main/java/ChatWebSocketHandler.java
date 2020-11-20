import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;

@WebSocket
public class ChatWebSocketHandler {

    private String sender, msg;

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        String username = "User" + Chat.nextUserNumber++;
        Chat.userUsernameMap.put(user, username);
        Chat.broadcastMessage(sender = "Server", msg = (username + " joined the chat"));
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        String username = Chat.userUsernameMap.get(user);
        Chat.userUsernameMap.remove(user);
        Chat.broadcastMessage(sender = "Server", msg = (username + " left the chat"));
        //清理数据
        Base64FileUtils.deleteAll("src/main/resources/public/img");
        Base64FileUtils.deleteAll("src/main/resources/public/file");
    }

    @OnWebSocketMessage
    //前端发来的msg
    public void onMessage(Session user, String message) {
        System.out.println(message);
        //一个简单的判断发来的是json还是字符串
        if (message.contains("'filename'")) {
            Chat.broadcastFile(sender = Chat.userUsernameMap.get(user), msg = message);
        } else {
            Chat.broadcastMessage(sender = Chat.userUsernameMap.get(user), msg = message);
        }
    }

}
