package com.mei.utils;

import com.mei.agora.media.RtcTokenBuilder;

import static com.mei.constant.AgoraConst.appCertificate;
import static com.mei.constant.AgoraConst.appId;

public class AgoraUtils {

    /**
     * 返回RtcToken
     *
     * @param channelName             频道名字
     * @param uid                     用户 ID
     * @param expirationTimeInSeconds 超时时间
     * @return
     */
    public static String getRtcToken(String channelName, int uid, int expirationTimeInSeconds) {
        RtcTokenBuilder token = new RtcTokenBuilder();
        int timestamp = (int) (System.currentTimeMillis() / 1000 + expirationTimeInSeconds);
        String result = token.buildTokenWithUid(appId, appCertificate,
                channelName, uid, RtcTokenBuilder.Role.Role_Publisher, timestamp);
        return result;
    }

    /**
     * 不指定超时时间：默认3600s
     * 不指定 uid ，不对 uid 鉴权
     *
     * @param channelName
     * @return
     */
    public static String getRtcToken(String channelName) {
        return getRtcToken(channelName, 0, 3600);
    }
}
