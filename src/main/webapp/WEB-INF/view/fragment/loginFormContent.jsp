<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="coffee_machine.controller.Parameters" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<table>
    <tr>
        <td><br><fmt:message key="login.email"/><br><br></td>
        <td><br><input type="text" name="<%=Parameters.LOGIN%>"><br><br></td>
    </tr>
    <tr>
        <td><br><fmt:message key="login.password"/><br><br></td>
        <td><br><input type="password" name="<%=Parameters.PASSWORD%>"><br><br></td>
    </tr>
    <tr>
        <td colspan="2">
            <fmt:message key="login.submit" var="mes_submit"/>
            <div><input type="submit" value="${mes_submit}"></div>
        </td>
    </tr>
</table>