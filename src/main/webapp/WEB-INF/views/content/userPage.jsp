<%--
  Created by IntelliJ IDEA.
  User: Telega
  Date: 20.02.2018
  Time: 8:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <title>UserPage</title>
</head>
<body>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-3.3.1.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/userPageScripts.js"></script>

    <jsp:include page="parts/header.jsp"/>

    <h3>page of ${user.username}</h3>

    <c:if test="${accessToPageContent}">
        <p id="pageOwnerName" style="background-color: crimson">${user.username}'s private content (Faggot porn and smth else)</p>
    </c:if>
    <br/>

    <c:if test="${accessToPageContent}">
        <form:form method="post" modelAttribute="feed" action="/service/createFeed">
            <form:label path="feedname">
                Feedname:
            </form:label>
            <form:input path="feedname"/>
            <input type="submit" value="create new feed">
        </form:form>
    </c:if>
    <br/>
    ${feeds}
</body>
</html>
