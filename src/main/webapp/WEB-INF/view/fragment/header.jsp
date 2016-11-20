<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="static coffee_machine.controller.PagesPaths.*" %>
<%@ page import="coffee_machine.controller.Parameters" %>
<%@ page import="coffee_machine.i18n.SupportedLocale" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<br>
<div align="center">
    <div style="max-width: 1200px" align="center">
        <%@ include file="/WEB-INF/view/fragment/i18n/header_block.jsp" %>
        <br>
        <div align="center">
            <h1><fmt:message key="title"/></h1>
            <br><br>

            <c:if test='${not empty sessionScope["admin_id"]}'>
                You logged in as administrator. You can <a href="<%=ADMIN_LOGOUT_PATH%>">logout</a>. Visit <a
                    href="<%=ADMIN_HOME_PATH%>">Admin Home Page</a><br>
            </c:if>
            <c:if test='${not empty sessionScope["user_id"]}'>
                You logged in as user. You can <a href="<%=USER_LOGOUT_PATH%>"><fmt:message key="logout"/></a>. Visit <a
                    href="<%=USER_HOME_PATH%>">User Home Page</a><br>
            </c:if>
            <br><br>
        </div>
        <br>
        <br>
