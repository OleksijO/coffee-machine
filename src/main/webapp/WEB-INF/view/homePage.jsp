<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page import="static coffee_machine.controller.PagesPaths.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title><fmt:message key="home.page"/></title>
</head>
<body>
<%@ include file="/WEB-INF/view/fragment/header.jsp" %>


        <fmt:message key="home.description"/><br>

        <br> <a href="<%=USER_LOGIN_PATH%>"><fmt:message key="login.user.title"/></a>
        <br>
        <br> <a href="<%=ADMIN_LOGIN_PATH%>"><fmt:message key="login.admin.title"/></a>


<%@ include file="/WEB-INF/view/fragment/footer.jsp" %>
</body>
</html>