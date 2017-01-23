<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="/WEB-INF/view/jsp/error/errorPage.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="coffee.machine.view.config.Parameters" %>
<%@ page import="coffee.machine.view.config.Attributes" %>
<%@ page import="coffee.machine.view.config.Paths" %>
<%@ page import="coffee.machine.model.entity.user.UserRole" %>
<hr>
<div align="center">
    <h4>
        <fmt:message key="${requestScope[Attributes.PAGE_TITLE]}"/>
        <br>
    </h4>
</div>
<hr>
<div align="center">
    |
    <a href="${pageContext.request.contextPath}${Paths.HOME_PATH}"><fmt:message key="home.page.href.name"/></a> |

    <c:if test='${empty sessionScope[Attributes.USER_ROLE]}'>
        <a href="${pageContext.request.contextPath}${Paths.LOGIN_PATH}"><fmt:message key="login.href.name"/></a> |
    </c:if>

    <c:if test='${sessionScope[Attributes.USER_ROLE] == UserRole.ADMIN}'>
        <a href="${pageContext.request.contextPath}${Paths.ADMIN_REFILL_PATH}"><fmt:message key="admin.refill.href.name"/></a> |
        <a href="${pageContext.request.contextPath}${Paths.ADMIN_ADD_CREDITS_PATH}"><fmt:message key="admin.add.credit.page"/></a> |
    </c:if>

    <c:if test='${sessionScope[Attributes.USER_ROLE] == UserRole.USER}'>
        <a href="${pageContext.request.contextPath}${Paths.USER_PURCHASE_PATH}"><fmt:message key="user.purchase.page"/></a> |
        <a href="${pageContext.request.contextPath}${Paths.USER_ORDER_HISTORY_PATH}"><fmt:message key="user.orders.history.page"/></a> |
    </c:if>

    <c:if test='${not empty sessionScope[Attributes.USER_ID]}'>
        <a href="${pageContext.request.contextPath}${Paths.LOGOUT_PATH}"><fmt:message key="logout"/></a> |
    </c:if>

    <br>
    <hr>
</div>


