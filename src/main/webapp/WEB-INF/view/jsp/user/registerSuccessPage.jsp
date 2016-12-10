<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/view/taglib/loginForm.tld" prefix="custom" %>
<%@ page import="coffee.machine.view.PagesPaths" %>
<%@ page import="coffee.machine.i18n.message.key.GeneralKey" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="/WEB-INF/view/jsp/fragment/header.jsp" %>

<br>
<fmt:message key="user.success.registration.description"/> <br>
<br>
${requestScope[Attributes.ADMIN_CONTACTS]}
<br>
<br>
<br> <h2><a href="${PagesPaths.USER_LOGIN_PATH}"><fmt:message key="login.user.href.name"/></a></h2>

<%@ include file="/WEB-INF/view/jsp/fragment/footer.jsp" %>
