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
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/csrfHeader.js" ></script>
    <script src="${pageContext.request.contextPath}/resources/js/userPageScriptsCreateFeed.js"></script>
</head>
<body >

<div class="bodyBack" id="bodyBack">
    <header>
        <jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/content/parts/headerAnonymous.jsp"/>
    </header>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/FeedOptions.js" ></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/IE.js" ></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/ScrollStop.js" ></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/userPageScriptsForLeftPanel.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/userPageScriptsForRightPanel.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/userPageScriptsForCenterPanel.js" ></script>

    <main>

        <div class="mainContainer">
            <div class="pagelayout">

                <%--Left panel with feeds--------------------------%>
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
                    <table id="tableFeeds" class="userFolders">
                        ${feedsHtml}
                    </table>

                    <div class="CreateFolderBlock">
                        <a href="#" onclick="showCreateFeedWindow()"><img src="${pageContext.request.contextPath}/resources/media/feeds/CreateFolder.png" alt=""></a>
                    </div>

                    <div class="MemoryLimitBlock">
                        <div class="MemoryLimitNone" >
                            <%--style="width: ${sizeBar}%;"--%>
                            <div class="MemoryLimitNoneStatus">
                            </div>
                        </div>

                        <div class="MemoryLimitNoneNumber"  >
                            <span>Занято <span id="leftPanelUserFilesSize">${user.filesSize}</span> GB из <span id="leftPanelUserSizeLimit">${user.sizeLimit}</span> GB</span>
                        </div>
                    </div>
                </div>

        <%--center part with posts-----------------------------------------------%>
            <div class="mainCenter" id="mainCenter">
            </div>


        <%--right part with upload block------------------------------------------%>
            <div class="mainRight">
                <div id="dropZone" class="DragFilePostBlock">
                    <input id="uploadFile" onchange="inputAction(event)" type="file" multiple> <img onclick="showUploadBlock()" src="${pageContext.request.contextPath}/resources/media/feeds/UploadDad.png" alt="">
                </div>

                <div id="uploadContainer" class="uploadContainer">
                    <div class="FeedHeaderUpload">
                        <img src="${pageContext.request.contextPath}/resources/media/feeds/nick_name_B.png" class="FeedLogoUpload">
                        <span class="FeedHeaderTextUpload">${authUsername}</span>
                    </div>
                    <div class="uploadBlock">
                        <div>
                            <span>Заметка</span>
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

</div>
<jsp:include page="parts/createFeedWindow.jsp"/>
</body>
</html>
