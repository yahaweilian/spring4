<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page contentType="text/html;charset=utf-8"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">  
<html>
   <head>
      <title>Spitter</title>
      <link rel="stylesheet" type="text/css" href="<c:url value="/resources/style.css"/>">
   </head>
   <body>
      <h1>Register</h1>
      <!-- 表单会根据commandName属性构建针对某个模型对象的上下文信息 -->
      <!-- 这里用的spring4的标签，换成spring 5 会报错，识别不了 commandName-->
      <!-- 需要依赖hibernate-validator包，否则error不起作用 -->
      <sf:form action="register" method="post" commandName="spitter" enctype="multipart/form-data">
      <sf:errors path="*" element="div" cssClass="errors" />
         <sf:label path="firstName" cssErrorClass="error">First Name:</sf:label> 
             <sf:input type="text" path="firstName" cssErrorClass="error" placeholder="请输入名字" /><br/>
         <sf:label path="lastName" cssErrorClass="error">Last Name: </sf:label>
             <sf:input type="text" path="lastName" cssErrorClass="error" placeholder="请输入姓氏"/><br/>
         <sf:label path="username" cssErrorClass="error">Username: </sf:label>
             <sf:input type="text" path="username" cssErrorClass="error" placeholder="请输入登陆名" /><br/>
         <sf:label path="password" cssErrorClass="error">Password: </sf:label>
             <sf:input type="password" path="password" cssErrorClass="error" placeholder="请输入密码"/><br/>
         <label>Profile Picture</label>:
             <input type="file" name="profilePicture" accept="image/jpeg,image/png,image/gif" /><br/>
         <input type="submit" value="Register">
      </sf:form>
   </body>
</html>