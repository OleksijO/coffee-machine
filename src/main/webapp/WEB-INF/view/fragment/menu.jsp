<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="static coffee_machine.controller.PagesPaths.*" %>
<%@ page import="coffee_machine.controller.Parameters" %>
<%@ page import="coffee_machine.i18n.SupportedLocale" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<c:set var="admin_id" value="<%=Attributes.ADMIN_ID%>"/>
<c:set var="user_id" value="<%=Attributes.USER_ID%>"/>

            <c:if test='${not empty sessionScope[admin_id]}'>
                <fmt:message key="logged.as.admin"/> |
                <a href="<%=ADMIN_HOME_PATH%>"><fmt:message key="admin.home.page"/></a> |
                <a href="<%=ADMIN_REFILL_PATH%>"><fmt:message key="admin.refill.page"/></a> |
                <a href="<%=ADMIN_LOGOUT_PATH%>"><fmt:message key="logout"/></a> |
                <br>
            </c:if>

            <c:if test='${not empty sessionScope["user_id"]}'>
                <fmt:message key="logged.as.user"/> |
                <a href="<%=USER_HOME_PATH%>"><fmt:message key="user.home.page"/></a> |
                <a href="<%=USER_HISTORY_PATH%>"><fmt:message key="user.history.page"/></a> |
                <a href="<%=USER_LOGOUT_PATH%>"><fmt:message key="logout"/></a> |
                <br>
            </c:if>




