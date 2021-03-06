<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="/WEB-INF/view/jsp/error/errorPage.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/view/taglib/loginForm.tld" prefix="custom" %>
<%@ page import="coffee.machine.view.config.Paths" %>
<%@ page import="coffee.machine.view.config.Pages" %>
<%@ page import="coffee.machine.view.config.Parameters" %>
<%@ page import="coffee.machine.view.config.Attributes" %>

<jsp:include page="${Pages.VIEW_JSP_CLASSPATH}/fragment/header.jsp" />
<br>
<custom:loginForm formTitleMessageKey="${requestScope[Attributes.LOGIN_FORM_TITLE]}"
                  cssClass="login_form"
                  action="${pageContext.request.contextPath}${requestScope[Attributes.LOGIN_FORM_ACTION]}"
                  loginLabelMessageKey="login.email"
                  parameterLogin="${Parameters.LOGIN_PARAM}"
                  loginPreviousValue="${requestScope[Attributes.PREVIOUS_ENTERED_EMAIL]}"
                  passwordLabelMessageKey="login.password"
                  parameterPassword="${Parameters.PASSWORD_PARAM}"
                  submitMessageKey="login.submit"
                  cancelMessageKey="cancel"
/>
<br>
<a href="${pageContext.request.contextPath}${Paths.USER_REGISTER_PATH}"><fmt:message key="register.user.href.name"/></a>
<jsp:include page="${Pages.VIEW_JSP_CLASSPATH}/fragment/footer.jsp" />
