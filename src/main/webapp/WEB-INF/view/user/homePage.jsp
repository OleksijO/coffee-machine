<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
			<fmt:message key="user.home.description"/>
			<br>Table with available goods with column to set wanted quantity to purchase. <br>
			<br> <a href="<%=HOME_PATH%>"><fmt:message key="home.page"/></a>
			<br> <a href="<%=USER_LOGOUT_PATH%>"><fmt:message key="logout"/></a>

<%@ include file="/WEB-INF/view/fragment/footer.jsp"%>
</body>
</html>