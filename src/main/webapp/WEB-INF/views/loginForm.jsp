<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<%@page contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" import="java.util.*" session="false"%>
<html>
   <head>
      <title>Spitter</title>
      <link rel="stylesheet" type="text/css" href='<c:out value="/resources/style.css" />' >   
   </head>
   <body>
      <h1>Login</h1>
      <!-- 表单会根据commandName属性构建针对某个模型对象的上下文信息 -->
      <!-- 这里用的spring4的标签，换成spring 5 会报错，识别不了 commandName-->
      <!-- 需要依赖hibernate-validator包，否则error不起作用 -->
      <sf:form action="login" method="post" commandName="loginUser">
      <sf:errors path="*" element="div" cssClass="errors" />
         <sf:label path="username" cssErrorClass="error">Username: </sf:label>
             <sf:input type="text" path="username" cssErrorClass="error" placeholder="Username" /><br/>
         <sf:label path="password" cssErrorClass="error">Password: </sf:label>
             <sf:input type="password" path="password" cssErrorClass="error" placeholder="Password" /><br/>
         <input type="submit" value="Sign in">
      </sf:form>
   </body>
</html>