<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="coffee_machine.controller.Parameters" %>
<%@ page import="coffee_machine.controller.Attributes" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<table>
    <tr>
        <td><br><label for="login"><fmt:message key="login.email"/></label><br><br></td>
        <td><br><input id="login" type="text" name="${Parameters.LOGIN}"
                       value="${sessionScope[Attributes.PREVIOUS_ENTERED_EMAIL]}"/><br><br></td>
    </tr>
    <tr>
        <td><br><label for="pswd"><fmt:message key="login.password"/></label><br><br></td>
        <td><br><input id="pswd" type="password" name="${Parameters.PASSWORD}"/><br><br></td>
    </tr>
    <tr>
        <td colspan="2">
            <fmt:message key="login.submit" var="message_submit"/>
            <div align="center"><input type="submit" value="${message_submit}"></div>
        </td>
    </tr>
</table>