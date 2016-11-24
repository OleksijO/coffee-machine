<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="coffee_machine.controller.PagesPaths" %>
<%@ page import="coffee_machine.controller.Attributes" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div align="center">
    <h4>
        <fmt:message key="${requestScope[Attributes.PAGE_TITLE]}"/>
        <br>
    </h4>
</div>
<div align="center">
    <a href="${PagesPaths.HOME_PATH}"><fmt:message key="home.page"/></a>

    <c:if test='${not empty sessionScope[Attributes.ADMIN_ID]}'>
        |
        <a href="${PagesPaths.ADMIN_HOME_PATH}"><fmt:message key="admin.home.page"/></a> |
        <a href="${PagesPaths.ADMIN_REFILL_PATH}"><fmt:message key="admin.refill.page"/></a> |
        <a href="${PagesPaths.ADMIN_LOGOUT_PATH}"><fmt:message key="logout"/></a> |

    </c:if>

    <c:if test='${not empty sessionScope[Attributes.USER_ID]}'>
        |
        <a href="${PagesPaths.USER_HOME_PATH}"><fmt:message key="user.home.page"/></a> |
        <a href="${PagesPaths.USER_PURCHASE_PATH}"><fmt:message key="user.purchase.page"/></a> |
        <a href="${PagesPaths.USER_HISTORY_PATH}"><fmt:message key="user.history.page"/></a> |
        <a href="${PagesPaths.USER_LOGOUT_PATH}"><fmt:message key="logout"/></a> |

    </c:if>
    <br> <br>
</div>

