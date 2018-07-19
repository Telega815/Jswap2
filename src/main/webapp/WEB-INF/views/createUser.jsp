<%--
  Created by IntelliJ IDEA.
  User: Telega
  Date: 28.02.2018
  Time: 15:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <title>Регистрация</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Style_header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Style_registration.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-3.3.1.js" ></script>
    <sec:csrfMetaTags />
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/csrfHeader.js" ></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/RegistrationScripts.js" ></script>
</head>
<body>

<div class="bodyBack">
    <header>
        <jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/content/parts/headerAnonymous.jsp"/>
    </header>

    <main>
<div class="mainBlock">
    <div id="entranceBlock">
        <p><spring:message code="registration"/></p>
    </div>

    <div id="centerBlock">
        <div class="centerBlockCenter">
            <form:form id="inputForm" class="inputForm" method="POST" modelAttribute="user">
                    <div class="logoBlockEntrance">
                        <img src="${pageContext.request.contextPath}/resources/media/registration/filled_star.png" alt="">
                        <spring:message var="login" code="login"/>
                        <form:input autocomplete="off" onchange="checkUsername(event)" path="username" id="inputField" type="text" placeholder="${login}" />
                        <img id="imgUsernameValid" class="checkedPhoto" src="${pageContext.request.contextPath}/resources/media/registration/checked.png" alt="">
                        <img id="imgUsernameInvalid" class="checkedPhotoError" src="${pageContext.request.contextPath}/resources/media/registration/errorChecked.png" alt="">
                        <p class="errorField"></p>
                    </div>
                    <div class="logoBlockEntrance">
                        <img src="${pageContext.request.contextPath}/resources/media/registration/filled_star.png" alt="">
                        <spring:message var="password" code="password"/>
                        <form:input autocomplete="off" onchange="checkPwd(event)" path="pwd" form="inputForm" id="inputFieldPassword" type="password" placeholder="${password}" />
                        <img id="imgPwdValid" class="checkedPhoto" src="${pageContext.request.contextPath}/resources/media/registration/checked.png" alt="">
                        <img id="imgPwdInvalid" class="checkedPhotoError" src="${pageContext.request.contextPath}/resources/media/registration/errorChecked.png" alt="">
                        <p class="errorField"></p>
                    </div>

                    <div class="logoBlockEntrance">
                        <img src="${pageContext.request.contextPath}/resources/media/registration/filled_star.png" alt="">
                        <spring:message var="repeatPwd" code="repeatPwd"/>
                        <input autocomplete="off" onchange="comparePwds(event)" id="inputFieldPasswordRepeat" type="password" placeholder="${repeatPwd}" />
                        <img id="imgPwdRepeatValid" class="checkedPhoto" src="${pageContext.request.contextPath}/resources/media/registration/checked.png" alt="">
                        <img id="imgPwdRepeatInvalid" class="checkedPhotoError" src="${pageContext.request.contextPath}/resources/media/registration/errorChecked.png" alt="">
                        <p class="errorField"></p>
                    </div>

                    <div class="logoBlockEntrance">
                        <img src="${pageContext.request.contextPath}/resources/media/registration/filled_star.png" alt="">
                        <spring:message var="email" code="email"/>
                        <form:input autocomplete="off" onchange="checkEmail(event)" path="email" id="inputEmail" type="email" placeholder="${email}" />
                        <img id="imgEmailValid" class="checkedPhoto" src="${pageContext.request.contextPath}/resources/media/registration/checked.png" alt="">
                        <img id="imgEmailInvalid" class="checkedPhotoError" src="${pageContext.request.contextPath}/resources/media/registration/errorChecked.png" alt="">
                        <p class="errorField"></p>
                    </div>
                    <p><spring:message code="emailClarification"/></p>

                    <div class="logoBlockEntrance">
                        <img src="${pageContext.request.contextPath}/resources/media/registration/filled_star.png" alt="">
                        <spring:message var="pincode" code="pincode"/>
                        <form:input pattern="[0-9]{4}" onchange="validPin(event)" id="inputPinCod" autocomplete="off" maxlength="4" path="pincode" type="password" placeholder="${pincode}" />
                        <img id="imgPincodeValid" class="checkedPhoto" src="${pageContext.request.contextPath}/resources/media/registration/checked.png" alt="">
                        <img id="imgPincodeInvalid" class="checkedPhotoError" src="${pageContext.request.contextPath}/resources/media/registration/errorChecked.png" alt="">
                        <p class="errorField"></p>

                    </div>
                    <p><spring:message code="pinClarification"/></p>
                <div id="CreateBlockEntrance">
                    <spring:message var="create" code="create"/>
                    <input disabled form="inputForm" id="submitRegForm" type="submit" value="${create}" >
                </div>
            </form:form>
        </div>
    </div>

</div>


</main>

<footer>
</footer>
</div>


<%--<form:form method="POST" modelAttribute="user">--%>


    <%--<fieldset>--%>

        <%--<form:label path="username">--%>
            <%--Username--%>
        <%--</form:label>--%>
        <%--<form:input path="username" />--%>

        <%--<form:label path="pwd">--%>
            <%--Password--%>
        <%--</form:label>--%>
        <%--<form:input path="pwd" />--%>

        <%--<form:label path="email">--%>
            <%--Email--%>
        <%--</form:label>--%>
        <%--<form:input path="email" />--%>

        <%--<form:label path="pincode">--%>
            <%--Pin-code--%>
        <%--</form:label>--%>
        <%--<form:input path="pincode" />--%>



    <%--</fieldset>--%>

    <%--<footer>--%>
        <%--<input type="submit" value="Create">--%>
    <%--</footer>--%>

<%--</form:form>--%>


</body>
</html>
