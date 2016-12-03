<jsp:directive.page errorPage="/error.html"/>
<%@page import="org.apache.log4j.Logger" %>
<%! static final Logger logger = Logger.getLogger("ERROR HANDLER"); %>
<% logger.error(String
        .format(ErrorMessage.JSP_HANDLER_MASSAGE_FORMAT,
                (int) pageContext.getSession().getAttribute(Attributes.USER_ID),
                (int) pageContext.getSession().getAttribute(Attributes.ADMIN_ID),
                pageContext.getErrorData().getStatusCode(),
                pageContext.getException().getMessage()), pageContext.getException()); %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/view/taglib/loginForm.tld" prefix="custom" %>
<%@ page import="coffee.machine.view.PagesPaths" %>
<%@ page import="coffee.machine.i18n.message.key.GeneralKey" %>
<%@ page import="coffee.machine.view.ErrorMessage" %>
<%@ page import="coffee.machine.view.Attributes" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="/WEB-INF/view/jsp/fragment/header.jsp" %>
<br>
<div align="center">
    <br><br><br><br>
    <h2><fmt:message key="internal.error"/></h2>
    <br>
    <h2><b> support@test.ua / 0-800-00-00 </b></h2>

</div>

<%@ include file="/WEB-INF/view/jsp/fragment/footer.jsp" %>
