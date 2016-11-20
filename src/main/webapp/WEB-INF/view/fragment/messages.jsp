<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="static coffee_machine.controller.PagesPaths.*" %>
<%@ page import="coffee_machine.controller.Attributes" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

        <c:set var="usual_message_attr" value="<%=Attributes.USUAL_MESSAGE%>"/>
        <c:set var="error_message_attr" value="<%=Attributes.ERROR_MESSAGE%>"/>
        <c:set var="err_message" value="${requestScope[error_message_attr]}"/>
        <c:set var="usual_message" value="${requestScope[usual_message_attr]}"/>

        <c:if test='${not empty err_message}'>

            <div align="center" style="width: 90%">
                <hr>
                <p style="color: red">
                        ${err_message}
                </p>
                <hr>
            </div>
        </c:if>

        <c:if test='${not empty usual_message}'>

            <div align="center" style="width: 90%">
                <hr>
                <p style="color: darkgreen">
                        ${usual_message}
                </p>
                <hr>
            </div>
        </c:if>




