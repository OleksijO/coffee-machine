<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="/WEB-INF/view/jsp/fragment/header.jsp" %>
<br>
<fmt:message key="home.description"/><br>
<c:if test='${empty sessionScope[Attributes.ADMIN_ID] && empty sessionScope[Attributes.USER_ID]}'>
    <h2>
        <br> <a href="${PagesPaths.USER_REGISTER_PATH}"><fmt:message key="register.user.title"/></a>
        <br>
        <br> <a href="${PagesPaths.USER_LOGIN_PATH}"><fmt:message key="login.user.title"/></a>
        <br>
        <br> <a href="${PagesPaths.ADMIN_LOGIN_PATH}"><fmt:message key="login.admin.title"/></a>
    </h2>
</c:if>
<%@ include file="/WEB-INF/view/jsp/fragment/footer.jsp" %>
