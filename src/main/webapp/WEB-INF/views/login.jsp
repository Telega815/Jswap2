<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="login"/></title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Style_header.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Style_login.css">
</head>

<body>
<div class="bodyBack">
	<header>
		<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/content/parts/header2.jsp"/>
	</header>

	<main>
		<div class="mainBlock">
			<div id="entranceBlock">
				<p><spring:message code="login"/></p>
			</div>
			<div id="ErrorField">
				<c:if test="${not empty error}">
				<p>${error}</p>
				</c:if>
			</div>
			<div id="centerBlock">
				<div id="logoBlock">
					<c:url value="/service/loginProcessing" var="loginUrl"/>
					<form id="inputForm" class="inputForm" name='form_login' action="${loginUrl}" method='POST'>
						<input type="hidden"
							   name="${_csrf.parameterName}"
							   value="${_csrf.token}"/>
					</form>
					<spring:message var="username" code="username"/>
					<input id="inputField" name='username' form="inputForm" type="text" placeholder="${username}" >
				</div>
				<div id="passwordBlock">
					<spring:message var="password" code="password"/>
					<input form="inputForm" id="inputFieldPassword" name='pwd' type="password" placeholder="${password}" >
				</div>
				<div id="rememberBlock">

					<form class="inputForm" action="user">
						<input  id="inputRememberMe" type="checkbox" ><label for="inputRememberMe"><spring:message code="rememberMe"/></label></form>

					<div id="ForgotYourPassword">
						<a href="#"><spring:message code="forgottenPassword"/></a>
					</div>
				</div>

				<div id="loginBlockLogo">
					<spring:message var="login" code="login"/>
					<input form="inputForm" id="submitFormLogin" type="submit" value="${login}" >
					<c:url value="/service/createUser" var="registrationPage"/>
					<a href="${registrationPage}"><spring:message code="createAcc"/></a>
				</div>
			</div>
		</div>
	</main>

	<footer>
	</footer>
</div>
</body>
</html>