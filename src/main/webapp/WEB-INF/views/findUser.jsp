<%--
  Created by IntelliJ IDEA.
  User: Telega
  Date: 20.02.2018
  Time: 8:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
    <title>FindUser</title>
</head>
<body>

    <c:url value="/service/findUser" var="findUser"/>
    <form:form method="post" action="${findUser}" modelAttribute="user" class="box Login">

        <c:if test="${not empty error}">
            ${error}
        </c:if>

        <fieldset>
            <form:label path="username">
                Username
            </form:label>
            <form:input path="username"/>
        </fieldset>

        <input type="submit" value="enter">
    </form:form>
    <c:url value="/service/login" var="loginPage"/>
    <a href="${loginPage}">fullyLogin</a>
    <c:url value="/service/createUser" var="registrationPage"/>
    <a href="${registrationPage}">createUser</a>
</body>
</html>
