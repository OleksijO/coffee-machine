<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="coffee_machine.controller.PagesPaths" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="/WEB-INF/view/fragment/header.jsp"%>


        <br>

        <form action="${PagesPaths.ADMIN_LOGIN_PATH}" method="post">
            <%@ include file="/WEB-INF/view/fragment/loginFormContent.jsp"%>
        </form>

        <br>


<%@ include file="/WEB-INF/view/fragment/footer.jsp" %>
