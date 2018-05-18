<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ page session="false"%>
<html>
<head>
<title>Spittr</title>
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/style.css"/>">
<script type="text/javascript" src="<c:url value="/resources/js/jquery/jquery.min.js"/>"></script>
<link rel="stylesheet" href="<c:url value="/resources/css/bootstrap/css/bootstrap.min.css"/>">
<!-- 可选的 Bootstrap 主题文件（一般不用引入） -->
<link rel="stylesheet" href="<c:url value="/resources/css/bootstrap/css/bootstrap-theme.min.css"/>">
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script type="text/javascript" src="<c:url value="/resources/js/bootstrap/bootstrap.min.js"/>"></script>

<script type="text/javascript">
var sock = new SockJS('spittr');
var stomp = Stomp.over(sock);
stomp.connect('guest','guest',function(frame){
	console.log('Connected');
	stomp.subscribe("/topic/spittlefeed",handleSpittle);
});

function handleSpittle(incoming){
	var spittle = JSON.parse(incoming.body);
	console.log('Received: ',spittle);
	var source = $("#spittle-template").html();
	var template = Handlebars.compile(source);
	var spittleHtml = template(spittle);
	$('.spittleList').prepend(spittleHtml);
}

</script>
<script id="spittle-template" type="text/x-handlebars-template">
<li id="preexist">
  <div class="spittleMessage">{{message}}</div>
  <div>
     <span class="spittleTime">{{time}}</span>
     <span class="spittleLocation">({{latitude}},{{longitude}})</span>
  </div>
</li>
</script>

</head>

<body>
	<!--s:message 国际化处理 -->
	<h1>
		<s:message code="spittr.welcome" />
	</h1>
	<a href="<c:url value="/spittles"/>">Spittles</a> |
	<a href="<c:url value="/spitter/register"/>">Register</a> |
	<a href="<c:url value="/spitter/login"/>">Sign in</a>

</html>