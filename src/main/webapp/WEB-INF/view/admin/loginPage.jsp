<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="static coffee_machine.controller.PagesPaths.ADMIN_LOGIN_SUBMIT_PATH" %>
<%@ page import="static coffee_machine.controller.PagesPaths.USER_LOGIN_PATH" %>
<%@ page import="static coffee_machine.controller.PagesPaths.HOME_PATH" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title><fmt:message key="login.admin.title"/></title>
</head>
<body>
<%@ include file="/WEB-INF/view/fragment/header.jsp"%>


        <br>
        <fmt:message key="login.admin.title"/><br>
        <form action="<%=ADMIN_LOGIN_SUBMIT_PATH%>" method="post">
            <%@ include file="/WEB-INF/view/fragment/loginFormContent.jsp"%>
        </form>

        <br>
        <br> <a href="<%=USER_LOGIN_PATH%>"><fmt:message key="login.user.title"/></a><br>
        <br> <a href="<%=HOME_PATH%>"><fmt:message key="home.page"/></a><br>

<%@ include file="/WEB-INF/view/fragment/footer.jsp" %>
</body>
</html>