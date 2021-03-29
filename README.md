# Instant Messaging (即时通讯)
本项目使用了Sparkjava作为后端服务器来实现即时通讯的功能 <br/>

参考了Sparkjava官方的两个案例代码来写的，具体参考代码地址如下： <br/>
websocket：[http://sparkjava.com/tutorials/websocket-chat](http://sparkjava.com/tutorials/websocket-chat) <br/>
file upload：[https://github.com/tipsy/spark-file-upload](https://github.com/tipsy/spark-file-upload)


### 主要功能
1. 可以发送文本信息、视频、音频
2. 可以发送zip、doc等文件
3. 使用 Agora.io(声网) 的sdk来实现在线视频通话
4. 有心跳机制，防止websocket掉线

### 尚未实现
1. 在线状态确认
2. 离线消息处理
3. 在线录制视频、语音

### 使用介绍
找到Main方法，点击运行，然后在浏览器中输入 http://localhost:4657 即可进入聊天页面

### 代码说明
1. 在 constant 包下，找到 AgoraConst类，输入自己的 appId 与 appCertificate。这两个参数在声网官网[https://docs.agora.io/cn](https://docs.agora.io/cn) 
注册一个自己的 **项目** 即可找到。
2. agora包 与 basicVideoCall文件夹，均是来自声网官方的参考代码，可以自己去下载完整代码进行学习

### 博文介绍
[https://blog.csdn.net/lendsomething/article/details/115297139](https://blog.csdn.net/lendsomething/article/details/115297139)
