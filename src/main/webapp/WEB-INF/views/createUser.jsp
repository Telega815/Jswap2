<%--
  Created by IntelliJ IDEA.
  User: Telega
  Date: 28.02.2018
  Time: 15:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
    <title>qwe</title>
</head>
<body>



<form:form method="POST" modelAttribute="user">


    <fieldset>

        <form:label path="username">
            Username
        </form:label>
        <form:input path="username" />

        <form:label path="pwd">
            Password
        </form:label>
        <form:input path="pwd" />

        <form:label path="email">
            Email
        </form:label>
        <form:input path="email" />

        <form:label path="pincode">
            Pin-code
        </form:label>
        <form:input path="pincode" />



    </fieldset>

    <footer>
        <input type="submit" value="Create">
    </footer>

</form:form>


</body>
</html>
