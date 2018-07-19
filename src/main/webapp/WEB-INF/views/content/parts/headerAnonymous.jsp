<%--
  Created by IntelliJ IDEA.
  User: Telega
  Date: 12.04.2018
  Time: 12:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <div class="header">
        <div class="header_left">
            <div id="LogoBlockHeader">
                <a href="/">
                    <img id="LogoBlockIMG" src="${pageContext.request.contextPath}/resources/media/header/5_4.png" alt=""></a>
            </div>
            <div id="languageBlock">
                <a href="?language=ru"><img src="${pageContext.request.contextPath}/resources/media/header/flagRus.png" alt="">RU</a>
                <a href="?language=en"><img src="${pageContext.request.contextPath}/resources/media/header/flagBrit.png" alt="">EN</a>
            </div>
            <div id="helpBlock">
                <a href="#"> <p><spring:message code="help"/></p></a>
            </div>
        </div>
        <div class="header_right">
            <div id="searchBlock">
                <spring:message var="search" code="search"/>
                <form id="searchForm" action="user"></form>
                <input form="searchForm" id="searchField" type="text" placeholder="${search}" >
                <input form="searchForm" id="submitSearchForm" type="submit" value="" >
            </div>
            <div id="loginBlock">
                <c:url value="/service/login" var="loginPage"/>
                <a href="${loginPage}"><spring:message code="login"/></a>
            </div>
            <div id="regBlock">
                <c:url value="/service/createUser" var="registrationPage"/>
                <a href="${registrationPage}"><spring:message code="createAcc"/></a>
            </div>
        </div>
    </div>

</body>
</html>
