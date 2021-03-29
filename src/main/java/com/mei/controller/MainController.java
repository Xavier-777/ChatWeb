package com.mei.controller;

import com.google.gson.Gson;
import com.mei.Main;
import com.mei.constant.PathConst;
import com.mei.entity.Agora;
import com.mei.entity.Message;
import com.mei.utils.AgoraUtils;
import com.mei.utils.BroadcastUtils;
import com.mei.utils.FileUtils;
import org.eclipse.jetty.websocket.api.Session;
import spark.Route;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import static com.mei.utils.BroadcastUtils.broadcastMessage;
import static spark.Spark.post;

public class MainController {

    public static void init() {
        post("/upload", upload);
        post("/token", token);
    }

    /**
     * 文件上传，该代码出自官网
     */
    private static Route upload = (request, response) -> {
        request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
        Part upload_file = request.raw().getPart("uploaded_file");      //获取前端上传的文件，这参数必须要与 input 标签的 name 一样

        InputStream input = upload_file.getInputStream();           //文件流
        String fileName = upload_file.getSubmittedFileName();       //文件名
        String prefix = fileName.substring(0, fileName.lastIndexOf("."));       //文件前缀名
        String suffix = fileName.substring(fileName.lastIndexOf("."), fileName.length());       //文件后缀名
        Path tempFile = Files.createTempFile(PathConst.uploadPath, prefix, suffix);         //创建临时文件
        Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);   //拷贝到本地

        //消息封装
        Message message = new Message();
        message.setContentTile(fileName);
        message.setMessageType(FileUtils.getFileType(suffix));
        message.setContent(tempFile.getFileName());     //这里特别注意！！
        message.setUserName(Main.userUsernameMap.get(Main.tempSender));
        Main.tempSender = null;

        BroadcastUtils.broadcastFile(message);    //广播文件
        return "upload success";
    };

    /**
     * 返回 agora 信息
     */
    private static Route token = ((request, response) -> {
        Agora agora = new Agora();
        agora.setChannel("100");
        agora.setToken(AgoraUtils.getRtcToken(agora.getChannel()));
        BroadcastUtils.broadcastMessage("Server", "We are having a rtc,please click start trc to join us");
        return new Gson().toJson(agora);
    });
}
