<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="static coffee_machine.controller.PagesPaths.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<br>
<br>
<div align="center">
    <h1> COFFEE-MACHINE </h1>
    <br><br>

    <c:if test='${not empty sessionScope["admin_id"]}'>
        You logged in as administrator. You can <br> <a href="<%=ADMIN_LOGOUT_PATH%>">logout</a>. Visit  <a href="<%=ADMIN_HOME_PATH%>">Admin Home Page</a><br>
    </c:if>
    <c:if test='${not empty sessionScope["user_id"]}'>
        You logged in as user. You can <br> <a href="<%=USER_LOGOUT_PATH%>">logout</a>. Visit  <a href="<%=USER_HOME_PATH%>">User Home Page</a><br>
    </c:if>
    <br><br>
</div>
<br>
<br>
