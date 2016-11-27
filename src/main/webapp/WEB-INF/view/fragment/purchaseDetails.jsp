<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="coffee_machine.controller.Attributes" %>
<%@ page import="coffee_machine.i18n.message.key.General" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<c:set var="history_record" value="${requestScope[Attributes.HISTORY_RECORD]}"/>

<c:if test='${not empty history_record}'>
    <div align="center" style="width: 90%; padding: 2px; margin: 2px; background: lightgreen; border: 3px darkgreen">
        <p>
            <fmt:message key="user.purchase.details"/>:<br>
            <fmt:message key="user.purchase.details.date"/> :<br>
            <fmt:formatDate type="both" value="${history_record.date}"/><br>
            <fmt:message key="user.purchase.details.description"/>${history_record.orderDescription}<br>
            <fmt:message key="user.purchase.details.amount"/> : ${history_record.realAmount} <fmt:message key="currency"/>
        </p>
    </div>
    <br>
    <hr>
    <br>
</c:if>





