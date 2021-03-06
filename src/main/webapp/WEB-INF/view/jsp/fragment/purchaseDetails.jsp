<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="/WEB-INF/view/jsp/error/errorPage.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="coffee.machine.view.config.Attributes" %>
<c:set var="order" value="${requestScope[Attributes.ORDER]}"/>

<c:if test='${not empty order}'>
    <div align="center" style="width: 90%; padding: 2px; margin: 2px; background: lightgreen; border: 3px darkgreen">
        <table>
            <tr>
                <td><strong><fmt:message key="user.purchase.details"/>:</strong></td>
                <td></td>
            </tr>
            <tr>
                <td><fmt:message key="user.purchase.details.date"/> :</td>
                <td>
                    <fmt:formatDate type="both" value="${order.date}"/></td>
            </tr>
            <tr>
                <td><fmt:message key="user.purchase.details.description"/>:</td>
                <td>
                    <c:forEach items="${order.drinks}" var="drink">
                        <c:if test="${drink.quantity gt 0}">
                            <strong>${drink.name}</strong> [${drink.quantity}]<br>
                            <c:forEach items="${drink.addons}" var="addon">
                                <c:if test="${addon.quantity gt 0}">
                                    <strong>+</strong>
                                    ${addon.name} [${addon.quantity}] <br>
                                </c:if>
                            </c:forEach>
                        </c:if>
                    </c:forEach>
                </td>
            </tr>
            <tr>
                <td><fmt:message key="user.purchase.details.amount"/> :</td>
                <td>${order.realTotalCost} <fmt:message key="currency"/></td>
            </tr>
        </table>
    </div>
    <br>
    <hr>
    <br>
</c:if>





