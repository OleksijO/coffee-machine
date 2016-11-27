<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="static coffee_machine.view.PagesPaths.*" %>
<%@ page import="coffee_machine.i18n.message.key.GeneralKey" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="/WEB-INF/view/jsp/fragment/header.jsp" %>
<div style="background: lightgray; border: 5px darkgray; max-width: 25% " align="center">
    <br>
    <b><fmt:message key="login.form.user.title"/></b><br>
    <hr>
    <form action="${PagesPaths.USER_LOGIN_PATH}" method="post">
        <%@ include file="/WEB-INF/view/jsp/fragment/loginFormContent.jsp" %>
    </form>
    <br>
</div>

<%@ include file="/WEB-INF/view/jsp/fragment/footer.jsp" %>
