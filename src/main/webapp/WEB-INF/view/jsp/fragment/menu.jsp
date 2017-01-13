<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
        <a href="${PagesPaths.HOME_PATH}"><fmt:message key="home.page.href.name"/></a> |
    <c:if test='${empty sessionScope[Attributes.ADMIN_ID] && empty sessionScope[Attributes.USER_ID]}'>
        <a href="${PagesPaths.LOGIN_PATH}"><fmt:message key="login.href.name"/></a> |
    </c:if>

    <c:if test='${not empty sessionScope[Attributes.ADMIN_ID]}'>
        <a href="${PagesPaths.ADMIN_REFILL_PATH}"><fmt:message key="admin.refill.href.name"/></a> |
        <a href="${PagesPaths.ADMIN_ADD_CREDITS_PATH}"><fmt:message key="admin.add.credit.page"/></a> |
        <a href="${PagesPaths.ADMIN_LOGOUT_PATH}"><fmt:message key="logout"/></a> |

    </c:if>

    <c:if test='${not empty sessionScope[Attributes.USER_ID]}'>
        <a href="${PagesPaths.USER_PURCHASE_PATH}"><fmt:message key="user.purchase.page"/></a> |
        <a href="${PagesPaths.USER_PURCHASE_OWN_PATH}"><fmt:message key="user.purchase.own.drink.page"/></a> |
        <a href="${PagesPaths.USER_ORDER_HISTORY_PATH}"><fmt:message key="user.orders.history.page"/></a> |
        <a href="${PagesPaths.USER_LOGOUT_PATH}"><fmt:message key="logout"/></a> |

    </c:if>

    <br>
    <hr>
</div>


