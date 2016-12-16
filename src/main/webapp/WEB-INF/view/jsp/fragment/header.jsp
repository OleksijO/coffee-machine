<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="coffee.machine.view.Parameters" %>
<%@ page import="coffee.machine.i18n.SupportedLocale" %>
<%@ page import="coffee.machine.view.Attributes" %>
<%@ page import="coffee.machine.config.CoffeeMachineConfig" %>
<%@ page import="coffee.machine.controller.RegExp" %>
<%@ page import="org.apache.log4j.Logger" %>
<%! static final Logger logger = Logger.getLogger("JSP ERROR HANDLER PAGE"); %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
         errorPage="/WEB-INF/view/jsp/error/errorPage.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <title><fmt:message key="${requestScope[Attributes.PAGE_TITLE]}"/></title>
</head>
<body style="background: gainsboro">

<div align="center">
    <br>
    <div class="container" align="center">

        <%@ include file="/WEB-INF/view/jsp/fragment/i18n/header_block.jsp" %>
        <hr>
        <br>
        <div align="center">

            <img src="${pageContext.request.contextPath}/img/coffee.png" height="100px" alt="Coffee Icon"><br>
            <h1><fmt:message key="title"/></h1>

            <%@ include file="/WEB-INF/view/jsp/fragment/menu.jsp" %>
            <%@ include file="/WEB-INF/view/jsp/fragment/messages.jsp" %>


        </div>

