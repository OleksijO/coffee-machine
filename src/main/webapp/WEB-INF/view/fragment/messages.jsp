<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="static coffee_machine.controller.PagesPaths.*" %>
<%@ page import="coffee_machine.controller.Attributes" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

        <c:set var="usual_message_attr" value="<%=Attributes.USUAL_MESSAGE%>"/>
        <c:set var="usual_message_additional_attr" value="<%=Attributes.USUAL_ADDITIONAL_MESSAGE%>"/>
        <c:set var="error_message_attr" value="<%=Attributes.ERROR_MESSAGE%>"/>
        <c:set var="error_message_additional_attr" value="<%=Attributes.ERROR_ADDITIONAL_MESSAGE%>"/>
        <c:set var="usual_message" value="${requestScope[usual_message_attr]}"/>
        <c:set var="usual_additional_message" value="${requestScope[usual_message_additional_attr]}"/>
        <c:set var="err_message" value="${requestScope[error_message_attr]}"/>
		<c:set var="error_additional_message" value="${requestScope[error_message_additional_attr]}"/>

<c:if test='${not empty err_message}'>
	<div align="center" style="width: 90%">
		<hr>
		<p style="color: red">
			<fmt:message key="${err_message}" />
			<c:if test='${not empty error_additional_message}'>
				<br> <fmt:message key="${error_additional_message}" />
            </c:if>
		</p>
		<hr>
	</div>
</c:if>

<c:if test='${not empty usual_message}'>

	<div align="center" style="width: 90%">
		<hr>
		<p style="color: darkgreen">
			<fmt:message key="${usual_message}" />
			<c:if test='${not empty usual_additional_message}'>
				<br> ${usual_additional_message}
            </c:if>
		</p>
		<hr>
	</div>
</c:if>




