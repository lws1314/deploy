var websocket = null;

var messageCallback;
//判断当前浏览器是否支持WebSocket
if ('WebSocket' in window) {
    websocket = new WebSocket("ws://"+ window.location.hostname +":"+ window.location.port +"/webSocket/admin");
    // websocket=new WebSocket("ws://localhost:1000/webSocket")
} else {
    if (messageCallback) {
        messageCallback("<span style='color: red'>当前浏览器 不支持 WebSocket</span>");
    }else{
        alert("当前浏览器 不支持 WebSocket");
    }
}
//连接发生错误的回调方法
websocket.onerror = function (event) {
    messageCallback("<span style='color: red;letter-spacing: 1px'>WebSocket连接发生错误</span>");
};
//连接成功建立的回调方法
websocket.onopen = function () {
    messageCallback("<span style='color: green;letter-spacing: 1px'>WebSocket连接成功</span>");
};

//接收到消息的回调方法
websocket.onmessage = function (event) {
    messageCallback(event.data);
};

//连接关闭的回调方法
websocket.onclose = function () {
    messageCallback("WebSocket连接关闭");
};

//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
window.onbeforeunload = function () {
    closeWebSocket();
};

//将消息显示在网页上
messageCallback = function messageProcessing(message) {
    console.log(message);
};

//关闭WebSocket连接
function closeWebSocket() {
    websocket.close();
}

//发送消息
function sendMessage(message) {
    websocket.send(message);
}

function msgCallback(callback) {
    if (typeof callback === "function") {
        messageCallback = callback;
    }
}