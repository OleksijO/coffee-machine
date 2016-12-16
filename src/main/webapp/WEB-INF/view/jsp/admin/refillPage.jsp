<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="coffee.machine.view.PagesPaths" %>
<%@ page import="coffee.machine.view.Attributes" %>
<%@ page import="coffee.machine.view.Parameters" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="/WEB-INF/view/jsp/fragment/header.jsp" %>
<br>
<fmt:message key="admin.refill.description"/> <br><br>
<fmt:message key="admin.refill.machine.balance"/> :
<fmt:formatNumber value="${requestScope[Attributes.COFFEE_MACHINE_BALANCE]}"
                  type="number"
                  minFractionDigits="2"
                  maxFractionDigits="2"/>
<fmt:message key="currency"/><br><br>

<form action="${PagesPaths.ADMIN_REFILL_PATH}" method="post">

    <table cellpadding="10" cellspacing="2" border="1">
        <tr>
            <td>
                <div align="center"><b><fmt:message key="refill.goods.name"/></b></div>
            </td>
            <td>
                <div align="center"><b><fmt:message key="refill.goods.available"/></b></div>
            </td>
            <td>
                <div align="center"><b><fmt:message key="refill.goods.add.quantity"/></b></div>
            </td>

        </tr>


        <tr>
            <td colspan="3">
                <div align="center"><b><fmt:message key="refill.base.drinks.title"/></b></div>
            </td>
        </tr>
        <c:forEach items="${requestScope[Attributes.DRINKS]}" var="drink">
            <tr>
                <td>${drink.name}</td>
                <td>
                    <div align="center">${drink.quantity}</div>
                </td>
                <td>
                    <div align="center">
                        <input type="number" step="1" min=0 name="${Parameters.DRINK_PARAMETER_STARTS_WITH}${drink.id}"
                               style="width: 30pt"
                               value="${empty requestScope[Attributes.PREVIOUS_VALUES_TABLE]?0:requestScope[Attributes.PREVIOUS_VALUES_TABLE].get(Parameters.DRINK_PARAMETER_STARTS_WITH.concat(drink.id))}">
                    </div>
                </td>
            </tr>
        </c:forEach>

        <tr>
            <td colspan="3">
                <div align="center"><b><fmt:message key="refill.addons.title"/></b></div>
            </td>
        </tr>
        <c:forEach items="${requestScope[Attributes.ADDONS]}" var="addon">
            <tr>
                <td>${addon.name}</td>
                <td>
                    <div align="center">${addon.quantity}</div>
                </td>
                <td>
                    <div align="center">
                        <input type="number" step="1" min="0"  name="${Parameters.ADDON_PARAMETER_STARTS_WITH}${addon.id}"
                               style="width: 30pt"
                               value="${empty requestScope[Attributes.PREVIOUS_VALUES_TABLE]?0:requestScope[Attributes.PREVIOUS_VALUES_TABLE].get(Parameters.ADDON_PARAMETER_STARTS_WITH.concat(addon.id))}">
                    </div>
                </td>
            </tr>
        </c:forEach>
        <tr>
            <td colspan="3">
                <br>
                <fmt:message key="refill.submit" var="message_submit"/>
                <div align="center"><input type="submit" value="${message_submit}"></div>
                <br>
            </td>
        </tr>
    </table>


    <br>
</form>


<%@ include file="/WEB-INF/view/jsp/fragment/footer.jsp" %>
