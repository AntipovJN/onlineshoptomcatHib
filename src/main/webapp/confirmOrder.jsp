<%--
  Created by IntelliJ IDEA.
  User: Eugene
  Date: 17.07.2019
  Time: 18:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    Enter authentication code from email, for confirm order
    <br/>Sum of the order: ${sum}
    ${error}
    <form action="/confirmOrder" method="post">
        <input type="hidden" value="${orderId}" name="id">
        Code: <input type="text" name="code">
        <input type="submit">
    </form>
</body>
</html>
