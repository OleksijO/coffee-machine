<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="/WEB-INF/view/jsp/error/errorPage.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="coffee.machine.view.config.Attributes" %>
<%@ page import="coffee.machine.view.config.Parameters" %>
<%@ taglib uri="/WEB-INF/view/taglib/Paginator.tld" prefix="custom" %>

<fmt:message key="paginator.first" var="first"/>
<fmt:message key="paginator.previous" var="previous"/>
<fmt:message key="paginator.next" var="next"/>
<fmt:message key="paginator.last" var="last"/>
<custom:paginator labelFirst="${first}"
                  labelPrevious="${previous}"
                  labelNext="${next}"
                  labelLast="${last}"
                  currentPageNumber="${requestScope[Attributes.CURRENT_PAGE]}"
                  lastPageNumber="${requestScope[Attributes.LAST_PAGE]}"
                  parameterPage="${Parameters.PAGE}"
/>
