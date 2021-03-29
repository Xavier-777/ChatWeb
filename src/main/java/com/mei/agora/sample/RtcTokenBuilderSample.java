package com.mei.agora.sample;

import com.mei.agora.media.RtcTokenBuilder;
import com.mei.agora.media.RtcTokenBuilder.Role;

/**
 *  Agora 官方测试生成 Token
 */
public class RtcTokenBuilderSample {
    static String appId = "970CA35de60c44645bbae8a215061b33";
    static String appCertificate = "5CFd2fd1755d40ecb72977518be15d3b";
    static String channelName = "7d72365eb983485397e3e3f9d460bdda";
    static int uid = 2082341273; //如果填 0，则表示不对 uid 鉴权
    static int expirationTimeInSeconds = 3600; //超时时间

    public static void main(String[] args) throws Exception {
        RtcTokenBuilder token = new RtcTokenBuilder();
        int timestamp = (int)(System.currentTimeMillis() / 1000 + expirationTimeInSeconds);
        String result = token.buildTokenWithUid(appId, appCertificate,
       		 channelName, uid, Role.Role_Publisher, timestamp);
        System.out.println(result);
    }
}
