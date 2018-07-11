<%--
  Created by IntelliJ IDEA.
  User: Telega
  Date: 20.02.2018
  Time: 8:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title>FindUser</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Style_header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Style_index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Style_footer.css">
</head>
<body>
<div class="bodyBack">

    <header>
        <jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/content/parts/header2.jsp"/>
    </header>

    <main>
        <div class="mainBlock">
            <div id="logoBlock">
                <img src="${pageContext.request.contextPath}/resources/media/main/5_4.png" alt="">
            </div>
            <div id="InputBlock">
                <spring:message var="enterUsername" code="enterUsername"/>
                <c:url value="/service/findUser" var="findUser"/>
                <form:form id="inputForm" action="${findUser}" modelAttribute="user" method="post">
                <form:input form="inputForm" id="inputField" type="text" placeholder="${enterUsername}" path="username"/>
                </form:form>
                <input form="inputForm" id="submitForm" type="submit" value="">
            </div>
            <br/>
            <div id="ErrorField">
                <div id="ErrorMessage">
            <c:if test="${not empty error}">
                <p>Пользователь не найден</p>
            </c:if>
                </div>
            </div>
        </div>
    </main>
    <footer>
        <div class="footer">
            <!-- <div id="footerBlock"> -->
            <img src="${pageContext.request.contextPath}/resources/media/main/2.png" alt="">
            <!-- </div> -->
        </div>
    </footer>
</div>
</body>
</html>