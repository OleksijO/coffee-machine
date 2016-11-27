<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="coffee_machine.controller.Attributes" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><fmt:message key="${requestScope[Attributes.PAGE_TITLE]}"/></title>
</head>
<body>
<br>
<div align="center">
    <div style="max-width: 1024px" align="center">
        <%@ include file="/WEB-INF/view/fragment/i18n/header_block.jsp" %>
        <hr>
        <br>
        <div align="center">

            <img src="${pageContext.request.contextPath}/img/coffee.png" height="100px" alt="Coffee Icon"><br>
            <h1><fmt:message key="title"/></h1>

            <%@ include file="/WEB-INF/view/fragment/menu.jsp" %>
            <%@ include file="/WEB-INF/view/fragment/messages.jsp" %>


        </div>

