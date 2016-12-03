<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="coffee.machine.view.Attributes" %>
<%@ page import="coffee.machine.i18n.message.key.GeneralKey" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<c:set var="history_record" value="${requestScope[Attributes.HISTORY_RECORD]}"/>

<c:if test='${not empty history_record}'>
    <div align="center" style="width: 90%; padding: 2px; margin: 2px; background: lightgreen; border: 3px darkgreen">
        <table>
            <tr>
                <td><b><fmt:message key="user.purchase.details"/>:</b></td>
                <td></td>
            </tr>
            <tr>
                <td><fmt:message key="user.purchase.details.date"/> :</td>
                <td>
                    <fmt:formatDate type="both" value="${history_record.date}"/></td>
            </tr>
            <tr>
                <td><fmt:message key="user.purchase.details.description"/>:</td>
                <td>${history_record.orderDescription}</td>
            </tr>
            <tr>
                <td><fmt:message key="user.purchase.details.amount"/> :</td>
                <td>${history_record.realAmount} <fmt:message key="currency"/></td>
            </tr>
        </table>
    </div>
    <br>
    <hr>
    <br>
</c:if>





