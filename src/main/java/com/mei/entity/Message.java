package com.mei.entity;

import com.mei.enums.MessageType;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 封装消息
 */
public class Message {
    private String userName;    //发送者
    private String sendDate = new SimpleDateFormat("HH:mm:ss").format(new Date());      //发送日期
    private String contentTile;
    private Path content;     //发送内容
    private MessageType messageType; //发送消息类型,详见枚举类

    public Message() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSendDate() {
        return sendDate;
    }

    public String getContentTile() {
        return contentTile;
    }

    public void setContentTile(String contentTile) {
        this.contentTile = contentTile;
    }

    public Path getContent() {
        return content;
    }

    public void setContent(Path content) {
        this.content = content;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    @Override
    public String toString() {
        return "Message{" +
                "userName='" + userName + '\'' +
                ", sendDate='" + sendDate + '\'' +
                ", contentTile='" + contentTile + '\'' +
                ", content=" + content +
                ", messageType=" + messageType +
                '}';
    }
}
