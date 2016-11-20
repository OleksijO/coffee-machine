
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page import="coffee_machine.controller.Parameters" %>
<%@ page import="coffee_machine.i18n.SupportedLocale" %>
<%@ page import="coffee_machine.controller.Attributes" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<div align="right">
    <c:set var="locale_attr" value="<%=Attributes.USER_LOCALE%>"/>
    <c:set var="fmt_locale" value="${sessionScope[locale_attr]}"/>
    <c:set var="locales" value="<%=SupportedLocale.values()%>"/>
    <c:forEach items="${locales}" var="locale">
        <c:if test="${locale.locale == fmt_locale}"><b>[</c:if>
        <a href="?<%=Parameters.USER_LOCALE%>=${locale.param}">${locale}</a>
        <c:if test="${locale.locale == fmt_locale}">]</b></c:if>
        &nbsp
    </c:forEach>
<!--
    <fmt:requestEncoding value="UTF-8" />
    <fmt:setLocale value="${fmt_locale}" scope="session"/>
-->
    <fmt:setBundle basename="i18n.messages"/>
</div>