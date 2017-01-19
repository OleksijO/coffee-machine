<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="coffee.machine.view.Attributes" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="/WEB-INF/view/jsp/fragment/header.jsp" %>
<br>
<fmt:message key="user.orders.history.description"/><br>
<br>


<table cellpadding="10" cellspacing="2" border="1">
    <tr>
        <td>
            <div align="center"><fmt:message key="user.orders.history.record.date"/></div>
        </td>
        <td>
            <div align="center"><fmt:message key="user.orders.history.record.order.description"/></div>
        </td>
        <td>
            <div align="center"><fmt:message key="user.orders.history.record.amount"/>, <fmt:message
                    key="currency"/></div>
        </td>
    </tr>

    <c:forEach items="${requestScope[Attributes.USER_ORDERS]}" var="order">
        <tr>

            <td>
                <div align="center"><fmt:formatDate type="both" value="${order.date}"/></div>
            </td>
            <td>
                <div align="left">
                    <c:forEach items="${order.drinks}" var="drink">
                        <c:if test="${drink.quantity gt 0}">
                            <b>${drink.name}</b> [${drink.quantity}]<br>
                            <c:forEach items="${drink.addons}" var="addon">
                                <c:if test="${addon.quantity gt 0}">
                                    <b>+</b>
                                    ${addon.name} [${addon.quantity}] <br>
                                </c:if>
                            </c:forEach>
                        </c:if>
                    </c:forEach></div>
            </td>
            <td>
                <div align="right"><fmt:formatNumber value="${order.realTotalCost}" type="number"
                                                     minFractionDigits="2" maxFractionDigits="2"/></div>
            </td>
        </tr>
    </c:forEach>

</table>


<%@ include file="/WEB-INF/view/jsp/fragment/footer.jsp" %>
