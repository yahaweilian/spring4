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
      <sf:form action="" method="post" commandName="spitter">
      <sf:errors path="*" element="div" cssClass="errors" />
         <sf:label path="firstName" cssErrorClass="error">First Name:</sf:label> 
             <sf:input type="text" path="firstName" cssErrorClass="error" /><br/>
         Last Name: <input type="text" name="lastName" /><br/>
         Username: <input type="text" name="username" /><br/>
         Password: <input type="password" name="password" /><br/>
         
         <input type="submit" value="Register">
      </sf:form>
   </body>
</html>