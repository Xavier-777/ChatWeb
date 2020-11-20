import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import static j2html.TagCreator.*;
import static spark.Spark.*;

public class Chat {

    // this map is shared between sessions and threads, so it needs to be thread-safe (http://stackoverflow.com/a/2688817)
    static Map<Session, String> userUsernameMap = new ConcurrentHashMap<>();
    static int nextUserNumber = 1; //Assign to username for next connecting user

    public static void main(String[] args) {
        String projectDir = System.getProperty("user.dir");
        String staticDir = "/src/main/resources/public";
        //自动刷新静态文件
        staticFiles.externalLocation(projectDir + staticDir);

        staticFiles.expireTime(600);
        webSocket("/chat", ChatWebSocketHandler.class);
        init();
    }


    //Sends a message from one user to all users, along with a list of current usernames
    //广播字符串
    public static void broadcastMessage(String sender, String message) {
        for (Session session : userUsernameMap.keySet()) {
            try {
                //发回给前端的所有session
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

    //广播文件
    public static void broadcastFile(String sender, String message) {
        JSONObject parseMsg = new JSONObject(message);
        String filename = (String) parseMsg.get("filename");//文件名
        String file = (String) parseMsg.get("file");//这个file以base64的形式传递
        String path = null;//路径

        //图片的操作：
        if (filename.contains(".jpg") || filename.contains(".jpeg") || filename.contains(".png")) {
            path = "src/main/resources/public/img/" + filename;
            try {
                Base64FileUtils.GenerateImage(file, path);
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (Session session : userUsernameMap.keySet()) {
                try {
                    session.getRemote().sendString(String.valueOf(new JSONObject()
                            .put("userMessage", createHtmlImgFromSender(sender, "img/" + filename))//userMessage是一个HTML标签
                            .put("userlist", userUsernameMap.values())
                    ));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            //文件的操作
            path = "src/main/resources/public/file/" + filename;
            try {
                Base64FileUtils.GenerateImage(file, path);
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (Session session : userUsernameMap.keySet()) {
                try {
                    session.getRemote().sendString(String.valueOf(new JSONObject()
                            .put("userMessage", createHtmlFileFromSender(sender, "file/" + filename, filename))//userMessage是一个HTML标签
                            .put("userlist", userUsernameMap.values())
                    ));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //Builds a HTML element with a sender-name, a message, and a timestamp,
    //展示字符串
    private static String createHtmlMessageFromSender(String sender, String message) {
        //返回一个HTML标签，这样的形式：
        // <article>
        //      <b>User1 says:</b>
        //      <span class="timestamp">20:16:21</span>
        //      <p>456</p>
        //</article>
        return article(
                b(sender + " says:"),
                span(attrs(".timestamp"), new SimpleDateFormat("HH:mm:ss").format(new Date())),
                p(message)
        ).render();
    }

    //Builds a HTML element with a sender-name, a file-path, and a timestamp,
    //展示图片
    private static String createHtmlImgFromSender(String sender, String path) {
        return article(
                b(sender + " says:"),
                span(attrs(".timestamp"), new SimpleDateFormat("HH:mm:ss").format(new Date())),
                img().withSrc(path)
        ).render();
    }

    //Builds a HTML element with a sender-name, a file-path, and a timestamp,
    //展示文件
    private static String createHtmlFileFromSender(String sender, String path, String filename) {
        return article(
                b(sender + " says:"),
                span(attrs(".timestamp"), new SimpleDateFormat("HH:mm:ss").format(new Date())),
                a(filename).withHref(path)
        ).render();
    }
}
