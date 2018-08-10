<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="http://cdn.sockjs.org/sockjs-0.3.min.js"></script>

<script type="text/javascript">

//var url = 'ws://' + window.location.host + '/websocket/macro';
//var sock = new WebSocket(url); //打开WebSocket
var url = 'macro';
var sock = new SockJS(url);

sock.onopen = function() {//处理连接开启事件
	console.log('Opening');
	sayMarco();
}

sock.onmessage = function(e){//处理信息
	console.log('Received message:',e.data);
	setTimeout(function(){sayMarco()},2000);
}

sock.onclose = function(){
	console.log('Closing'); //处理连接关闭事件
}

function sayMarco(){
	console.log('Sending Marco!');
	sock.send("Marco!"); //发送消息
}

</script>


<title>Insert title here</title>
</head>
<body>

</body>
</html>