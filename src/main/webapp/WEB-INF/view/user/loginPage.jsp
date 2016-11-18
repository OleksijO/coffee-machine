<%@ page import="static coffee_machine.controller.PagesPaths.*" %>
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
            <br>Вход в пользовательский раздел<br>
            <form action="<%=USER_LOGIN_SUBMIT_PATH%>" method="post">
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
			<br> <a href="<%=ADMIN_LOGIN_PATH%>">Log in as Administrator</a>
			<br> <a href="<%=HOME_PATH%>">Home page</a>
		</div>
	</div>
<%@ include file="/WEB-INF/view/fragment/footer.jsp"%>
</body>
</html>