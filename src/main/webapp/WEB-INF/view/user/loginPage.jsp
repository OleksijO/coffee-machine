<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="static coffee_machine.controller.PagesPaths.*" %>
<%@ page import="coffee_machine.i18n.message.key.General" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="/WEB-INF/view/fragment/header.jsp"%>

            <br>
            <fmt:message key="login.form.user.title"/><br>
            <br>
            <form action="${PagesPaths.USER_LOGIN_PATH}" method="post">
                <%@ include file="/WEB-INF/view/fragment/loginFormContent.jsp"%>
            </form>
            <br>


<%@ include file="/WEB-INF/view/fragment/footer.jsp"%>
