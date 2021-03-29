package com.mei.utils;

import com.mei.entity.Message;
import com.mei.enums.MessageType;

import java.text.SimpleDateFormat;
import java.util.Date;

import static j2html.TagCreator.*;

public class HtmlUtils {
    /**
     * 展示信息
     * 返回一个HTML标签，这样的形式：
     * <article>
     * <b>User1 says:</b>
     * <span class="timestamp">20:16:21</span>
     * <p>456</p>
     * </article>
     *
     * @param sender
     * @param message
     * @return
     */
    public static String createHtmlMessageFromSender(String sender, String message) {

        return article(
                b(sender + " says:"),
                span(attrs(".timestamp"), new SimpleDateFormat("HH:mm:ss").format(new Date())),
                p(message)
        ).render();
    }

    /**
     * 根据不同的 消息类型 展示不同的html
     *
     * @param message
     * @return
     */
    public static String createHtml(Message message) {
        if (message.getMessageType().equals(MessageType.Image))
            return ImgHtml(message);
        else if (message.getMessageType().equals(MessageType.Audio))
            return AudioHtml(message);
        else if (message.getMessageType().equals(MessageType.Video))
            return VideoHtml(message);
        else
            return OtherHtml(message);
    }


    //展示图片
    public static String ImgHtml(Message message) {
        return "<article>" +
                "   <b>" + message.getUserName() + " says:</b>" +
                "   <span class=\"timestamp\">" + message.getSendDate() + "</span>" +
                "   <img src='" + message.getContent() + "'/>" +
                "</article>";
    }

    //展示音频
    public static String AudioHtml(Message message) {
        return "<article>" +
                "   <b>" + message.getUserName() + " says:</b>" +
                "   <span class=\"timestamp\">" + message.getSendDate() + "</span>" +
                "   <audio src='" + message.getContent() + "' controls=\"controls\" />" +
                "</article>";
    }

    //展示视频
    public static String VideoHtml(Message message) {
        return "<article>" +
                "   <b>" + message.getUserName() + " says:</b>" +
                "   <span class=\"timestamp\">" + message.getSendDate() + "</span>" +
                "   <video src='" + message.getContent() + "' controls=\"controls\"/>" +
                "</article>";
    }


    //展示其他文件
    public static String OtherHtml(Message message) {
        return "<article>" +
                "   <b>" + message.getUserName() + " says:</b>" +
                "   <span class=\"timestamp\">" + message.getSendDate() + "</span>" +
                "   <a href='" + message.getContent() + "'>" + message.getContentTile() + "</a>" +
                "</article>";
    }
}
