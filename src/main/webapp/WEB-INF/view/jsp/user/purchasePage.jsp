<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="/WEB-INF/view/jsp/error/errorPage.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="coffee.machine.view.config.Paths" %>
<%@ page import="coffee.machine.view.config.Pages" %>
<%@ page import="coffee.machine.view.config.Parameters" %>
<%@ page import="coffee.machine.view.config.Attributes" %>

<jsp:include page="${Pages.VIEW_JSP_CLASSPATH}/fragment/header.jsp" />
<jsp:include page="${Pages.VIEW_JSP_CLASSPATH}/fragment/purchaseDetails.jsp" />
<br>
<fmt:message key="user.purchase.description"/> <br>
<br>
<form action="${pageContext.request.contextPath}${Paths.USER_PURCHASE_PATH}" method="post">
    <fmt:message key="user.purchase.your.balance.is"/>
    <fmt:formatNumber value="${requestScope[Attributes.USER_BALANCE]}" type="number"
                      minFractionDigits="2" maxFractionDigits="2"/>
    <fmt:message key="currency"/>.<br>
    <c:if test="${requestScope[Attributes.USER_BALANCE] le requestScope[Attributes.BALANCE_LOW_WARN_LIMIT]}">
        <br>
        <p style="color: red"><fmt:message
                key="user.purchase.your.balance.too.low.contact.administrator.to.add.credits"/>:
                ${requestScope[Attributes.ADMIN_CONTACTS]}
        </p>
    </c:if>

    <br>
    <table cellpadding="10" cellspacing="2" border="1">
        <tr>

            <td>
                <div align="center"><strong><fmt:message key="purchase.drink.name"/></strong></div>
            </td>
            <td>
                <div align="center"><strong><fmt:message key="purchase.drink.price"/>, <fmt:message key="currency"/></strong>
                </div>
            </td>
            <td>
                <div align="center"><strong><fmt:message key="purchase.drink.quantity"/></strong></div>
            </td>
        </tr>

        <c:forEach items="${requestScope[Attributes.DRINKS]}" var="drink">
            <c:if test="${drink.quantity gt 0}">
                <tr>
                    <td><strong>${drink.name}</strong>
                        <table>
                            <c:set var="addons" value="${drink.addons}"/>

                            <c:forEach items="${addons}" var="addon">
                                <c:if test="${addon.quantity gt 0}">
                                    <tr>
                                        <td>
                                        <td>
                                            <strong>+</strong>
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
                                                <c:set var="previousAddonQuantity" value="${empty requestScope[Attributes.PREVIOUS_VALUES_TABLE]?
                                                                0:requestScope[Attributes.PREVIOUS_VALUES_TABLE]
                                                                .get(Parameters.DRINK_PARAMETER_STARTS_WITH
                                                                .concat(drink.id)
                                                                .concat(Parameters.ADDON_PARAMETER_STARTS_WITH)
                                                                .concat(addon.id))}"/>
                                                <c:choose>
                                                    <c:when test="${previousAddonQuantity gt addon.quantity}">
                                                        <option selected>0</option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:if test="${previousAddonQuantity gt 0}">
                                                            <option selected>${previousAddonQuantity}</option>
                                                        </c:if>
                                                        <option value="0">0</option>
                                                    </c:otherwise>
                                                </c:choose>
                                                <option value="1">1</option>
                                                <c:if test="${addon.quantity gt 1}">
                                                    <option value="2">2</option>
                                                </c:if>
                                            </select>
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
                        <div align="center">
                            <input type="number" step="1" min="0" max="${drink.quantity}"
                                   name="${Parameters.DRINK_PARAMETER_STARTS_WITH.concat(drink.id)}"
                                   style="width: 30pt"
                                   value="${empty requestScope[Attributes.PREVIOUS_VALUES_TABLE]?0:requestScope[Attributes.PREVIOUS_VALUES_TABLE]
                                   .get(Parameters.DRINK_PARAMETER_STARTS_WITH.concat(drink.id))}">
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

<jsp:include page="${Pages.VIEW_JSP_CLASSPATH}/fragment/footer.jsp" />
