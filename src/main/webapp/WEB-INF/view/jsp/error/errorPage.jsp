<jsp:directive.page errorPage="/error.html" />
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/view/taglib/loginForm.tld" prefix="custom" %>
<%@ page import="coffee_machine.view.PagesPaths" %>
<%@ page import="coffee_machine.i18n.message.key.GeneralKey" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="/WEB-INF/view/jsp/fragment/header.jsp" %>

<div align="center">
    <br><br><br><br>
    <h2><fmt:message key="internal.error"/></h2>
    <br>
    <h2><b> support@test.ua / 0-800-00-00 </b></h2>

</div>

<%@ include file="/WEB-INF/view/jsp/fragment/footer.jsp" %>
