//Establish the WebSocket connection and set up event handlers
var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chat");
//客户端接收服务端数据
webSocket.onmessage = function (msg) {
    updateChat(msg);//解析msg，并更新聊天列表
};
webSocket.onclose = function () {
    alert("WebSocket connection closed")
};

//Send message if "Send" is clicked
//点击发送
id("send").addEventListener("click", function () {
    sendMessage(id("message").value);
    sendFile(id("file").files);
});

//Send message if enter is pressed in the input field
//敲回车发送
id("message").addEventListener("keypress", function (e) {
    if (e.keyCode === 13) {
        sendMessage(e.target.value);
    }
});

//Send a message if it's not empty, then clear the input field
function sendMessage(message) {
    if (message !== "") {
        webSocket.send(message);//发送数据给服务器
        id("message").value = "";//发送完之后清空内容
    }
}

//发送文件
var file_name = null;

function sendFile(fileList) {
    var jsonStr = null;
    for (var i = 0; i < fileList.length; i++) {
        file_name = fileList[i].name;//获取文件名

        var reader = new FileReader();
        reader.readAsDataURL(fileList[i]);//读取文件
        //文件读取完毕后该函数响应
        reader.onload = function loaded(evt) {
            var binaryString = evt.target.result;
            var start = binaryString.indexOf("base64") + 7;//base64以后的数字
            var end = binaryString.length;
            var base64File = binaryString.substring(start, end);//切割该文件
            jsonStr = `{'filename':'${file_name}','file':'${base64File}'}`;//拼接json
            webSocket.send(jsonStr);
        }
    }
    id("file").value = "";//发送完后清空
}


/*----------------------------------------------------------------------------*/
//Update the chat-panel（聊天列表）, and the list of connected users
//这个msg是一个json类型的MessageEvent，其中data才是我们后端发送给前端的信息
function updateChat(msg) {
    console.log(msg);
    var data = JSON.parse(msg.data);
    insert("chat", data.userMessage);//后端给的userMessage
    id("userlist").innerHTML = "";//设置id为userlist标签之间的 文本 为空
    data.userlist.forEach(function (user) {//这个user是userlist这个数组里面的个体
        insert("userlist", "<li>" + user + "</li>");
    });
}

//Helper function for inserting HTML as the first child of an element
function insert(targetId, message) {
    id(targetId).insertAdjacentHTML("afterbegin", message);
}

//Helper function for selecting element by id
function id(id) {
    return document.getElementById(id);
}