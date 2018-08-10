<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<script type="text/javascript">
var url = 'http://' + window.location.host + '/stomp/marcopolo';

var sock = new SockJS(url);//创建SockJS连接

var stomp = Stomp.over(sock);//创建STOMP客户端

var payload = JSON.stringify({'message' : 'Marco!'});

stomp.connect('guest','guest',function(frame){//连接STOMP客户端	
	stomp.send("/macro",{},payload);//发送消息
});

</script>



</head>
<body>

</body>
</html>