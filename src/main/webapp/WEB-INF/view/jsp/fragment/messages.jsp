<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="/WEB-INF/view/jsp/error/errorPage.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="coffee.machine.view.config.Attributes" %>

<c:set var="usual_message" value="${requestScope[Attributes.USUAL_MESSAGE]}"/>
<c:set var="usual_additional_message" value="${requestScope[Attributes.USUAL_ADDITIONAL_MESSAGE]}"/>
<c:set var="err_message" value="${requestScope[Attributes.ERROR_MESSAGE]}"/>
<c:set var="error_additional_message" value="${requestScope[Attributes.ERROR_ADDITIONAL_MESSAGE]}"/>
<c:set var="validation_errors" value="${requestScope[Attributes.VALIDATION_ERRORS]}"/>

<c:if test='${not empty err_message}'>
    <div align="center" style="width: 90%; padding: 2px; margin: 2px; background: lightpink; border: 3px red">
       <p>
            <fmt:message key="${err_message}"/>
            <c:if test='${not empty error_additional_message}'>
                <br> ${error_additional_message}
            </c:if>
        </p>
    </div>
</c:if>

<c:if test='${not empty validation_errors}'>
    <c:forEach var="validation_error_key" items="${validation_errors}">
    <div align="center" style="width: 90%; padding: 2px; margin: 2px; background: lightpink; border: 3px red;">
        <p>
            <fmt:message key="${validation_error_key}"/>
        </p>
    </div>
    </c:forEach>
</c:if>

<c:if test='${not empty usual_message}'>
    <div align="center" style="width: 90%; padding: 2px;margin: 2px; background: lightgreen; border: 3px darkgreen">
        <p>
            <fmt:message key="${usual_message}"/>
            <c:if test='${not empty usual_additional_message}'>
                <br> ${usual_additional_message}
            </c:if>
        </p>
    </div>
</c:if>

<c:if test='${not empty usual_message or not empty err_message}'>
    <hr>
    <br>
</c:if>





