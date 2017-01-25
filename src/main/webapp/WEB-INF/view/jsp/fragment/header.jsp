<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="/WEB-INF/view/jsp/error/errorPage.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="coffee.machine.view.config.Pages" %>
<%@ page import="coffee.machine.view.config.Attributes" %>
<%@ page import="org.apache.log4j.Logger" %>
<%! static final Logger logger = Logger.getLogger("JSP ERROR HANDLER PAGE"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/img/favicon.ico">
    <title>
    <fmt:message key="${requestScope[Attributes.PAGE_TITLE]}"/>
    </title>
</head>
<body style="background: gainsboro">

<div align="center">
    <br>
    <div class="container" align="center">

        <jsp:include page="${Pages.VIEW_JSP_CLASSPATH}/fragment/i18n/header_block.jsp" />
        <hr>
        <br>
        <div align="center">

            <img src="${pageContext.request.contextPath}/img/coffee.png" height="100px" alt="Coffee Icon"><br>
            <h1><fmt:message key="title"/></h1>

            <jsp:include page="${Pages.VIEW_JSP_CLASSPATH}/fragment/menu.jsp" />
            <jsp:include page="${Pages.VIEW_JSP_CLASSPATH}/fragment/messages.jsp" />


        </div>

