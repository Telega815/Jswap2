<%--
  Created by IntelliJ IDEA.
  User: Telega
  Date: 04.04.2018
  Time: 16:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <title>FeedPage</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/pinCode.css">
</head>
<body>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-3.3.1.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/feedPageScripts.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/PinCode.js"></script>
<jsp:include page="parts/PinCode.jsp"/>
<jsp:include page="parts/header.jsp"/>

<h3>page of ${feeds.user.username}</h3>
<h3>Feed: ${feeds.feedname}</h3>



<form:form id="chooseFilesForm" method="post" modelAttribute="uploadedFile" enctype="multipart/form-data" >

    <table>
        <tr>
            <td>Upload File:</td>
            <td><input onchange="sendFilesSeparatly(event)" type="file" id="files" name="files" multiple/></td>
        </tr>
    </table>
</form:form>
<input id="uploadButton" type="button" value="upload(BUTTON)" onclick="sendFiles()">
<input id="enterPinButton" type="button" value="enterPin" onclick="PinInputShow()">
<br/>
    <%--<progress id="progressbar" value="0" max="100"></progress>--%>
<br/>
<ul id="fileList">

</ul>
<textarea id="comment" name="comment" rows="3">

</textarea>
<input type="button" value="save" onclick="saveFiles()">

<a href="${feeds.feedname}/viewFiles" id="viewFilesHref">viewFiles</a>
<form action="">
    <div id="dropZone" style="width: 300px; height: 300px; background-color: #545b5f">
    </div>
</form>
</body>
</html>
