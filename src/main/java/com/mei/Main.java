package com.mei;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.mei.constant.PathConst;
import com.mei.controller.MainController;
import com.mei.handler.ChatWebSocketHandler;
import com.mei.utils.FileUtils;
import org.eclipse.jetty.websocket.api.Session;

import static spark.Spark.*;

public class Main {

    public static Map<Session, String> userUsernameMap = new ConcurrentHashMap<>();
    public static int nextUserNumber = 1; //Assign to username for next connecting user
    public static Session tempSender = null;

    public static void main(String[] args) {
        staticFileLocation("/public");
        webSocket("/chat", ChatWebSocketHandler.class);

        FileUtils.FileSystemInit(PathConst.uploadDir);
        staticFiles.externalLocation(PathConst.uploadDir);

        before("/*", (request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Content-Type", "application/json;charset=utf-8");
            response.header("Cache-Control", "no-cache");
        });

        MainController.init();
        System.out.println("start success,please click http://localhost:4567 to use");
    }
}
