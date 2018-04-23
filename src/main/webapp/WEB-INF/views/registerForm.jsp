<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<%@ page session="false" %>
<html>
   <head>
      <title>Spitter</title>
      <link rel="stylesheet" type="text/css" href='<c:out value="/resources/style.css" />' >   
   </head>
   <body>
      <h1>Register</h1>
      <!-- 表单会根据commandName属性构建针对某个模型对象的上下文信息 -->
      <!-- 这里用的spring4的标签，换成spring 5 会报错，识别不了 commandName-->
      <sf:form action="register" method="post" commandName="spitter" enctype="multipart/form-data">
      <sf:errors path="*" element="div" cssClass="errors" />
         <sf:label path="firstName" cssErrorClass="error">First Name:</sf:label> 
             <sf:input type="text" path="firstName" cssErrorClass="error" /><br/>
         Last Name: <input type="text" name="lastName" /><br/>
         Username: <input type="text" name="username" /><br/>
         Password: <input type="password" name="password" /><br/>
         <label>Profile Picture</label>:
             <input type="file" name="profilePicture" accept="image/jpeg,image/png,image/gif" /><br/>
         <input type="submit" value="Register">
      </sf:form>
   </body>
</html>