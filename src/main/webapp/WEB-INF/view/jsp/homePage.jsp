<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="/WEB-INF/view/jsp/error/errorPage.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="coffee.machine.view.config.Pages" %>

<jsp:include page="${Pages.VIEW_JSP_CLASSPATH}/fragment/header.jsp" />

<br>
<fmt:message key="home.description"/><br>

<jsp:include page="${Pages.VIEW_JSP_CLASSPATH}/fragment/footer.jsp" />
