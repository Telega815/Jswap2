<%--
  Created by IntelliJ IDEA.
  User: Telega
  Date: 27.03.2018
  Time: 15:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<body>
    <header style="background-color: aquamarine">hi
        <sec:authorize access="isAuthenticated()">
            <sec:authentication property="principal.username"/>
        </sec:authorize>
        <sec:authorize access="isAnonymous()">
            Unknown faggot
        </sec:authorize>
        go fuck yourself!

        <sec:authorize access="isAuthenticated()">
            <a href="${pageContext.request.contextPath}/service/logout">Logout</a>
        </sec:authorize>
        <sec:authorize access="isAuthenticated()">
            <a href="${pageContext.request.contextPath}/<sec:authentication property="principal.username"/>">To my page</a>
        </sec:authorize>
        <sec:authorize access="isAnonymous()">
            <a href="${pageContext.request.contextPath}/service/login">login</a>
        </sec:authorize>


    </header>
</body>
</html>
