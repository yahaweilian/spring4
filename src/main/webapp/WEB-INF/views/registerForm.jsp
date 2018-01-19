<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
   <head>
      <title>Spitter</title>
      <link rel="stylesheet" type="text/css" href='<c:out value="/resources/style.css" />' />   
   </head>
   <body>
      <h1>Register</h1>
      
      <form action="" method="post">
         First Name: <input type="text" name="firstName" /><br/>
         Last Name: <input type="text" name="lastName" /><br/>
         Username: <input type="text" name="username" /><br/>
         Password: <input type="password" name="password" /><br/>
         
         <input type="submit" value="Register">
      </form>
   </body>
</html>