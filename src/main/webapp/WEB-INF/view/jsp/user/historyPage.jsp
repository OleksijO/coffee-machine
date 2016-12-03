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
            <div align="center"><fmt:message key="user.history.record.date"/></div>
        </td>
        <td>
            <div align="center"><fmt:message key="user.history.record.order.description"/></div>
        </td>
        <td>
            <div align="center"><fmt:message key="user.history.record.amount"/>, <fmt:message key="currency"/></div>
        </td>
    </tr>

    <c:forEach items="${requestScope[Attributes.USER_RECORD_HISTORY_LIST]}" var="record">
        <tr>

            <td>
                <div align="center"><fmt:formatDate type="both" value="${record.date}"/></div>
            </td>
            <td>
                <div align="left"> ${record.orderDescription} </div>
            </td>
            <td>
                <div align="right"> <fmt:formatNumber value="${record.realAmount}" type="number"
                                                      minFractionDigits="2" maxFractionDigits="2"/> </div>
            </td>
        </tr>
    </c:forEach>

</table>


<%@ include file="/WEB-INF/view/jsp/fragment/footer.jsp" %>
