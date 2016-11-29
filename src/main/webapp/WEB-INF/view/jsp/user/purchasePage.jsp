<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="coffee_machine.view.PagesPaths" %>
<%@ page import="coffee_machine.view.Parameters" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="/WEB-INF/view/jsp/fragment/header.jsp" %>
<%@ include file="/WEB-INF/view/jsp/fragment/purchaseDetails.jsp" %>
<br>
<fmt:message key="user.purchase.description"/> <br>
<br>
<form action="${PagesPaths.USER_PURCHASE_PATH}" method="post">
    <fmt:message key="user.purchase.your.balance.is"/> <fmt:formatNumber value="${user_balance}" type="number"
                                                                         minFractionDigits="2" maxFractionDigits="2"/>
    <fmt:message key="currency"/>.<br>
    <br>
    <table cellpadding="10" cellspacing="2" border="1">
        <tr>

            <td>
                <div align="center"><b><fmt:message key="purchase.drink.name"/></b></div>
            </td>
            <td>
                <div align="center"><b><fmt:message key="purchase.drink.price"/>, <fmt:message key="currency"/></b>
                </div>
            </td>
            <td>
                <div align="center"><b><fmt:message key="purchase.drink.available"/></b></div>
            </td>
            <td>
                <div align="center"><b><fmt:message key="purchase.drink.quantity"/></b></div>
            </td>
        </tr>

        <c:set var="drinks_attr" value="<%=Attributes.REFILL_DRINKS%>"/>
        <c:forEach items="${requestScope[drinks_attr]}" var="drink">
            <c:if test="${drink.quantity gt 0}">
                <tr>
                    <td><b>${drink.name}</b>
                        <table>
                            <c:set var="addons" value="${drink.addons}"/>

                            <c:forEach items="${addons}" var="addon">
                                <c:if test="${addon.quantity gt 0}">
                                    <tr>
                                        <td>
                                        <td test='${addon.quantity>0}'>
                                            <b>+</b>
                                                ${addon.name}</td>
                                        <td>(+<fmt:formatNumber value="${addon.realPrice}"
                                                                type="number"
                                                                minFractionDigits="2"
                                                                maxFractionDigits="2"/>)
                                        </td>
                                        <td>
                                            <select
                                                    name="${Parameters.DRINK_PARAMETER_STARTS_WITH
                                                    .concat(drink.id)
                                                    .concat(Parameters.ADDON_PARAMETER_STARTS_WITH)
                                                     .concat(addon.id)}"
                                                    style="width: 30pt">
                                                <option selected value="0">0</option>
                                                <option value="1">1</option>
                                                <c:if test="${addon.quantity gt 1}">
                                                    <option value="2">2</option>
                                                </c:if>
                                            </select>
                                        </td>
                                        <td>
                                            [${addon.quantity}]
                                        </td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                        </table>
                    </td>
                    <td>
                        <div align="center"><fmt:formatNumber value="${drink.realPrice}" type="number"
                                                              minFractionDigits="2" maxFractionDigits="2"/></div>
                    </td>
                    <td>
                        <div align="center">${drink.quantity}</div>
                    </td>
                    <td>
                        <div align="center">
                            <input type="text" value="0"
                                   name="<%=Parameters.DRINK_PARAMETER_STARTS_WITH%>${drink.id}"
                                   style="width: 20pt">
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
