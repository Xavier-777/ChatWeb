package com.mei.entity;

import com.mei.constant.AgoraConst;

/**
 * rtc 所需三要素
 */
public class Agora {
    private String appid = AgoraConst.appId;
    private String token;
    private String channel;

    public Agora() {
    }

    public Agora(String token, String channel) {
        this.token = token;
        this.channel = channel;
    }

    public String getAppid() {
        return appid;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "Agora{" +
                "appid='" + appid + '\'' +
                ", token='" + token + '\'' +
                ", channel='" + channel + '\'' +
                '}';
    }
}
