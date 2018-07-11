<%--
  Created by IntelliJ IDEA.
  User: Telega
  Date: 06.04.2018
  Time: 13:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>pin</title>
</head>
<body>
    <div class="b-popup" id="pinBlock">
        <div class="b-popup-content">
            <label>PIN: </label>
            <input id="pinInputField" type="password" onkeyup="countPinInput(event)">
            <input type="button" value="submit" onclick="checkPin(event)">
        </div>
    </div>
</body>
</html>
