<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/view/taglib/loginForm.tld" prefix="custom" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="/WEB-INF/view/jsp/fragment/header.jsp" %>

<br>
<fmt:message key="user.success.registration.description"/> <br>
<br>
${requestScope[Attributes.ADMIN_CONTACTS]}
<br>


<%@ include file="/WEB-INF/view/jsp/fragment/footer.jsp" %>
