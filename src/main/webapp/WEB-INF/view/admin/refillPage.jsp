<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="coffee_machine.controller.PagesPaths" %>
<%@ page import="coffee_machine.controller.Attributes" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="/WEB-INF/view/fragment/header.jsp" %>

<fmt:message key="admin.refill.description"/> <br><br>
<fmt:message key="admin.refill.machine.balance"/> :
${requestScope[Attributes.COFFEE_MACHINE_BALANCE]}
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
        <c:forEach items="${requestScope[Attributes.REFILL_DRINKS]}" var="drink">
            <tr>
                <td>${drink.name}</td>
                <td>
                    <div align="center">${drink.quantity}</div>
                </td>
                <td>
                    <div align="center">
                        <input type="text" value="0" name="${Parameters.DRINK_PARAMETER_STARTS_WITH}${drink.id}"
                               style="width: 20pt">
                    </div>
                </td>
            </tr>
        </c:forEach>

        <tr>
            <td colspan="3">
                <div align="center"><b><fmt:message key="refill.addons.title"/></b></div>
            </td>
        </tr>
        <c:forEach items="${requestScope[Attributes.REFILL_ADDONS]}" var="addon">
            <tr>
                <td>${addon.name}</td>
                <td>
                    <div align="center">${addon.quantity}</div>
                </td>
                <td>
                    <div align="center">
                        <input type="text" value="0" name="${Parameters.ADDON_PARAMETER_STARTS_WITH}${addon.id}"
                               style="width: 20pt">
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


<%@ include file="/WEB-INF/view/fragment/footer.jsp" %>
