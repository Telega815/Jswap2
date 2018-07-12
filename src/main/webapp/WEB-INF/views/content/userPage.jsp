<%--
  Created by IntelliJ IDEA.
  User: Telega
  Date: 20.02.2018
  Time: 8:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <title>UserPage</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Style_header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Style_feeds.css">

    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-3.3.1.js" ></script>
    <sec:csrfMetaTags />
    <script type="text/javascript" language="javascript">
        var headers = {};

        var csrfParameter = $("meta[name='_csrf_parameter']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        var csrfToken = $("meta[name='_csrf']").attr("content");

        headers[csrfHeader] = csrfToken;
    </script>
</head>
<body >
<div class="bodyBack" id="bodyBack">
    <header>
        <jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/content/parts/header2.jsp"/>
    </header>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/userPageScripts.js" ></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/UploadScripts.js" ></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/FeedOptions.js" ></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/IE.js" ></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/ScrollStop.js" ></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/userPageScriptsForRightPanel.js"></script>

    <main>

        <div class="mainContainer">
            <div class="pagelayout">
                <div class="userContainer">
                    <div class="userHeader">
                        <div class="userHeaderLeft">
                            <img src="${pageContext.request.contextPath}/resources/media/feeds/nick_name_B.png" alt="" class="userLogo">
                            <div class="userText">
                                <p class="userHeaderText">${user.username.toUpperCase()}</p>
                                <p class="userHeaderText">Ленты</p>
                            </div>
                        </div>
                        <a href="#"><img src="${pageContext.request.contextPath}/resources/media/feeds/UserFeedEdit.png" alt="" class="userLogoEdit"></a>
                    </div>
                    <div class="NoFeeds">
                        <c:if test="${haventFeeds == false}">
                            <span> У Вас нет лент </span>
                        </c:if>
                    </div>
                    <table class="userFolders">
                        ${feeds}
                    </table>
                    <div class="CreateFolderBlock">
                        <a href="#"><img src="${pageContext.request.contextPath}/resources/media/feeds/CreateFolder.png" alt=""></a>
                    </div>
                    <c:set var="sizeLimitGB" value="${user.sizeLimit/1024/1024/1024}"/>
                    <div class="MemoryLimitBlock">
                        <div class="MemoryLimitNone" >
                            <div class="MemoryLimitNoneStatus" style="width: ${sizeBar}%;" >
                            </div>
                        </div>

                        <div class="MemoryLimitNoneNumber"  >
                            <span>Занято ${sizeGB} GB из ${sizeLimitGB} GB</span>
                        </div>
                    </div>


                </div>
            <div class="mainCenter" id="mainCenter">
                ${posts}
            </div>



            <div class="mainRight">
                <div id="dropZone" class="DragFilePostBlock">
                    <input id="uploadFile" onchange="inputAction(event)" type="file" multiple> <img onclick="showUploadBlock()" src="${pageContext.request.contextPath}/resources/media/feeds/UploadDad.png" alt="">
                </div>

                <div id="uploadContainer" class="uploadContainer">
                    <div class="FeedHeaderUpload">
                        <img src="${pageContext.request.contextPath}/resources/media/feeds/nick_name_B.png" class="FeedLogoUpload">
                        <span class="FeedHeaderTextUpload">${user.username.toUpperCase()}</span>
                    </div>
                    <div class="uploadBlock">
                        <div class="upload">
                            <span>Загружать в</span>
                            <select id="selectFeed">
                                ${feedsAsOptions}
                            </select>
                        </div>
                        <div>
                            <span>Заметка</span>
                            <%--<textarea id="uploadComment" name="comment" rows="3"></textarea>--%>
                        </div>
                        <div id="uploadComment" class="comment" contenteditable="true" onmouseover="preventScrollTheNote()" onmouseout="returnScroll()"></div>
                        <div class="uploadStatus"  onmouseover="preventScroll()" onmouseout="returnScroll()">
                        <table id="uploadTable">

                        </table>
                        </div>
                    </div>

                    <div id="innerDropZone" class="uploadDadBlock">
                        <input id="myfileUpload" onchange="inputAction(event)" type="file" multiple> <img onclick="myfileUpload.click();" src="${pageContext.request.contextPath}/resources/media/feeds/UploadDad.png" alt="">
                    </div>
                    <div class="SubmitBottonUploadBlock">
                        <input onclick="saveFiles()" id="uploadBlockInputSubmit" type="submit" value="Сохранить">
                    </div>
                </div>

            </div>
        </div>
        </div>

    </main>

    <footer>
    </footer>
</div>
</body>
</html>
