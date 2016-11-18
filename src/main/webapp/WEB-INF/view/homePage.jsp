<%@ page import="static coffee_machine.controller.PagesPaths.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Insert title here</title>
</head>
<body>
<%@ include file="/WEB-INF/view/fragment/header.jsp"%>
<div align="center">
    <div style="max-width: 1200px" align="center">
        <br> <a href="<%=USER_LOGIN_PATH%>">Log in as User</a>
        <br> <a href="<%=ADMIN_LOGIN_PATH%>">Log in as Administrator</a>

    </div>
</div>
<%@ include file="/WEB-INF/view/fragment/footer.jsp"%>
</body>
</html>