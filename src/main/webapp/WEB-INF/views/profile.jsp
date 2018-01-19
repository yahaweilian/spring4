<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
   <body>
      <h1>Your Profile</h1>
      <c:out value="${spitter.username }" /> <br/>
      <c:out value="${spitter.fristName }" /> 
      <c:out value="${spitter.lastName }" /> 
      
   </body>
</html>