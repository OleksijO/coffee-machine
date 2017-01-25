<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"  %>
<%@ page errorPage="/WEB-INF/view/jsp/error/errorPage.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="coffee.machine.view.config.Parameters" %>
<%@ page import="coffee.machine.i18n.SupportedLocale" %>
<%@ page import="coffee.machine.view.config.Attributes" %>
<%-- there is no need to use fmt setLocale, because native session scoped attribute already set in LocaleFilter --%>
<fmt:requestEncoding value="UTF-8" />
<fmt:setBundle basename="${sessionScope[Attributes.BUNDLE_FILE]}"/>

<div align="right">
    <c:forEach items="${SupportedLocale.values()}" var="locale">
        <c:choose>
            <c:when test="${locale.locale == sessionScope[Attributes.USER_LOCALE]}">
                <strong>
                ${locale}
                </strong>
            </c:when>
            <c:otherwise>
                <a href="?${Parameters.USER_LOCALE}=${locale.param}">${locale}</a>
            </c:otherwise>
        </c:choose>
    </c:forEach>
</div>