<%@ page import="static coffee_machine.controller.PagesPaths.ADMIN_LOGIN_SUBMIT_PATH" %>
<%@ page import="static coffee_machine.controller.PagesPaths.USER_LOGIN_PATH" %>
<%@ page import="static coffee_machine.controller.PagesPaths.HOME_PATH" %>
<%@ page import="static coffee_machine.controller.Parameters.LOGIN" %>
<%@ page import="static coffee_machine.controller.Parameters.PASSWORD" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Insert title here</title>
</head>
<body>
<%@ include file="/WEB-INF/view/fragment/header.jsp"%>
<div align="center">
    <div style="max-width: 1200px" align="center">
        <br>
        <br>Вход в административный раздел<br>
        <form action="<%=ADMIN_LOGIN_SUBMIT_PATH%>" method="post">
            <table>
                <tr>
                    <td><br>Логин<br><br></td>
                    <td><br><input type="text" name="<%=LOGIN%>"><br><br></td>
                </tr>
                <tr>
                    <td><br>Пароль<br><br></td>
                    <td><br><input type="password" name="<%=PASSWORD%>"><br><br></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div><input type="submit" value="Submit"></div>
                    </td>
                </tr>
            </table>
        </form>

        <br>
        <br> <a href="<%=USER_LOGIN_PATH%>">Log in as User</a><br>
        <br> <a href="<%=HOME_PATH%>">Home page</a><br>
    </div>
</div>
<%@ include file="/WEB-INF/view/fragment/footer.jsp"%>
</body>
</html>