<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="static coffee.machine.view.PagesPaths.*" %>
<%@ page import="static coffee.machine.view.Attributes.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="/WEB-INF/view/jsp/fragment/header.jsp" %>
<br>
<fmt:message key="user.history.description"/><br>
<br>


<table cellpadding="10" cellspacing="2" border="1">
    <tr>
        <td>
            <div align="center"><fmt:message key="user.history.record..id"/></div>
        </td>
        <td>
            <div align="center"><fmt:message key="user.history.record.date"/></div>
        </td>
        <td>
            <div align="center"><fmt:message key="user.history.record.order.description"/></div>
        </td>
        <td>
            <div align="center"><fmt:message key="user.history.record.amount"/>, <fmt:message key="currency"/></div>
        </td>
    </tr>

    <c:forEach items="${requestScope[Attributes.USER_RECORD_HISTORY_LIST]}" var="drink">
        <tr>
            <td>
                <div align="center">${drink.id}</div>
            </td>
            <td>
                <div align="left"><fmt:formatDate type="both" value="${drink.date}"/></div>
            </td>
            <td>
                <div align="left"> ${drink.orderDescription} </div>
            </td>
            <td>
                <div align="right"> <fmt:formatNumber value="${drink.realAmount}" type="number"
                                                      minFractionDigits="2" maxFractionDigits="2"/> </div>
            </td>
        </tr>
    </c:forEach>

</table>


<%@ include file="/WEB-INF/view/jsp/fragment/footer.jsp" %>
