<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="coffee_machine.view.PagesPaths" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="/WEB-INF/view/jsp/fragment/header.jsp" %>
<br>
<fmt:message key="home.description"/><br>
<h2>
    <br> <a href="${PagesPaths.USER_LOGIN_PATH}"><fmt:message key="login.user.title"/></a>
    <br>
    <br> <a href="${PagesPaths.ADMIN_LOGIN_PATH}"><fmt:message key="login.admin.title"/></a>
</h2>
<%@ include file="/WEB-INF/view/jsp/fragment/footer.jsp" %>
