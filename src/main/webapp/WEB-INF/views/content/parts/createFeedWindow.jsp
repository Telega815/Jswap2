<%--
  Created by IntelliJ IDEA.
  User: gur01
  Date: 24.07.2018
  Time: 15:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <head>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/Style_createFeed.css">
        <title></title>
    </head>
</head>
<body>
    <div class="positionDiv" id="createFeedWindow">
    <div class="feedCreating">
        <div class="FeedHeaderCreate">
            <span class="FeedHeaderTittle">Создание ленты</span>
            <img onclick="hideCreateFeedWindow()" src="${pageContext.request.contextPath}/resources/media/feeds/x.png" alt="">
        </div>
        <div class="feedCreatingName">
            <span>Feed name:</span>
            <input id="feedName" oninput="checkFeedName(event)"  type="text" placeholder="Feed name" />
        </div>
        <div id="feedCreatingNameError">
            <span id="feedCreatingErrorSpan">Feed already exists</span>
        </div>
        <div class="feedCreatingOptions">
            <span id="advancedOptions">Additional settings</span>
            <div id="advancedOptionsShow">
                <div class="showBlocks showBlock1">
                    <span>Read</span>
                    <select id="modeRead">
                        <option>Free read</option>
                        <option>pin-code required</option>
                        <option>Full authentication required</option>
                    </select>
                </div>
                <div class="showBlocks showBlock1">
                    <span>Write</span>
                    <select id="modeWrite">
                        <option>Free write</option>
                        <option>pin-code required</option>
                        <option>Full authentication required</option>
                    </select>
                </div>
                <div class="showBlocks showBlock2">
                    <span>Restrict space for feed to: </span>
                    <input id="limitSize" type="number" name="" placeholder="0" onKeyPress="return check(event,value)" onInput="checkLength()"/>
                    <select id="sizeType">

                        <option>Mb</option>
                        <option>Gb</option>

                    </select>
                </div>
            </div>
        </div>
        <div class="feedCreatingButtons">
            <%--<button class="feedCreatingButtonsDelete">Удалить ленту</button>--%>
            <button class="feedCreatingButtonsSave disableButton"  onclick="sendFormCreateFeed()">Сохранить</button>
        </div>
    </div>
    </div>
</body>
</html>
