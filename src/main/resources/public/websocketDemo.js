//确认发送者的字符串，用UUID生成的
var sender = "6e28cc9715cd4cb09c3d8c0789fe361f";

//心跳检测的字符串，也是UUID生成的
var heart = "790c1e4f11134c73b8f1c1c11b448f83";

//ws://localhost:4567/chat    这个 /chat 是 main 方法中的 websocket() 的path
var webSocket = new WebSocket(`ws://${location.hostname}:${location.port}/chat`);
webSocket.onopen = function () {
    heartCheck.reset();  //心跳检测
};
//接收服务端数据
webSocket.onmessage = function (msg) {
    var data = JSON.parse(msg.data);
    if (data.heartCheck) {
        console.log(data); //获取心跳json
    } else {
        updateChat(data);//解析json，并更新聊天列表
    }
    heartCheck.reset(); //心跳检测
};
webSocket.onclose = function () {
    alert("WebSocket connection closed")
};


$(function () {
    $("#upload_btn").click(function () {
            if ($("#upload_file").get(0).files.length != 0) {
                //确认发送者
                webSocket.send(sender);
                $.ajax({
                    type: 'post'
                    , url: `http://${location.hostname}:${location.port}/upload`
                    , data: new FormData($("#upload_form")[0])
                    , cache: false
                    , processData: false
                    , contentType: false
                }).done(function (res) {
                    console.log(res);
                }).fail(function (res) {
                    // 这里我没有搞懂为什么响应信息是走 fail 这里的
                    $("#upload_file").val("");
                    console.log(res.responseText);
                });
            }
        }
    );
});

//心跳包
var heartCheck = {
    timeout: 30000,        //30秒发一次心跳
    timeoutObj: null,
    serverTimeoutObj: null,
    reset: function () {
        clearTimeout(this.timeoutObj);
        clearTimeout(this.serverTimeoutObj);
        //return this;
        this.start();
    },
    start: function () {
        var self = this;
        this.timeoutObj = setTimeout(function () {
            //这里发送一个心跳，后端收到后，返回一个心跳消息，
            //onmessage拿到返回的心跳就说明连接正常
            webSocket.send(heart);
            self.serverTimeoutObj = setTimeout(function () {//如果超过一定时间还没重置，说明后端主动断开了
                webSocket.close();     //如果onclose会执行reconnect，我们执行ws.close()就行了.如果直接执行reconnect 会触发onclose导致重连两次
            }, self.timeout)
        }, this.timeout)
    }
};

//这个msg是一个json类型的数据，后端发来的，其中msg.data才是我们后端发送给前端的信息
function updateChat(data) {
    insert("chat", data.userMessage);//后端给的userMessage
    id("userlist").innerHTML = "";//设置id为userlist标签之间的 文本 为空
    data.userlist.forEach(function (user) {//这个user是userlist这个数组里面的个体
        insert("userlist", "<li>" + user + "</li>");
    });
}

//点击发送
id("send").addEventListener("click", function () {
    sendMessage(id("message").value);
});

//敲回车发送
id("message").addEventListener("keypress", function (e) {
    if (e.keyCode === 13) {
        sendMessage(e.target.value);
    }
});

//发送Text消息给服务器，并清空列表
function sendMessage(message) {
    if (message !== "") {
        webSocket.send(message);
        id("message").value = "";
    }
}

//Helper function for inserting HTML as the first child of an element
function insert(targetId, message) {
    id(targetId).insertAdjacentHTML("afterbegin", message);
}

//Helper function for selecting element by id
function id(id) {
    return document.getElementById(id);
}