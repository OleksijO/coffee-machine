<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="/WEB-INF/view/jsp/fragment/header.jsp" %>
<%@ include file="/WEB-INF/view/jsp/fragment/purchaseDetails.jsp" %>
<br>
<fmt:message key="user.purchase.own.drink.description"/> <br>
<br>
<form action="${PagesPaths.USER_PURCHASE_OWN_PATH}" method="post">

    <br>
    <fmt:message key="user.purchase.own.drink.select.base.drink"/>:
    <select
            name="${Parameters.DRINK_PARAMETER_STARTS_WITH}"
            style="width: 30%; min-width: 300pt">

        <c:forEach items="${requestScope[Attributes.DRINKS]}" var="drink">
            <c:if test="${drink.quantity gt 0}">
                <option value="${drink.id}">${drink.name}</option>
            </c:if>
        </c:forEach>

    </select>
    <br><br>

    <fmt:message key="user.purchase.own.drink.select.addons"/>:<br>
    <table cellpadding="10" cellspacing="2" border="1">
        <tr>

            <td>
                <div align="center"><b><fmt:message key="purchase..own.drink.addons"/></b></div>
            </td>
            <td>
                <div align="center"><b><fmt:message key="purchase.drink.price"/>, <fmt:message key="currency"/></b>
                </div>
            </td>
            <td>
                <div align="center"><b><fmt:message key="purchase.drink.quantity"/></b></div>
            </td>
        </tr>

        <c:forEach items="${requestScope[Attributes.ADDONS]}" var="addon">
            <c:if test="${addon.quantity gt 0}">
                <tr>
                    <td>${addon.name}<td>
                        <div align="center"><fmt:formatNumber value="${addon.realPrice}" type="number"
                                                              minFractionDigits="2" maxFractionDigits="2"/></div>
                    </td>
                    <td>
                        <div align="center">
                            <input type="number" step="1" min="0" max="${addon.quantity}"
                                   name="${Parameters.ADDON_PARAMETER_STARTS_WITH.concat(addon.id)}"
                                   style="width: 30pt"
                                   value="${empty requestScope[Attributes.PREVIOUS_VALUES_TABLE]?0:requestScope[Attributes.PREVIOUS_VALUES_TABLE]
                                   .get(Parameters.ADDON_PARAMETER_STARTS_WITH.concat(addon.id))}">
                        </div>
                    </td>
                </tr>
            </c:if>
        </c:forEach>

        <tr>
            <td colspan="4">
                <br>
                <fmt:message key="user.purchase.submit" var="message_submit"/>
                <div align="center"><input type="submit" value="${message_submit}"></div>
                <br>
            </td>
        </tr>
    </table>

    <br>
</form>

<%@ include file="/WEB-INF/view/jsp/fragment/footer.jsp" %>
