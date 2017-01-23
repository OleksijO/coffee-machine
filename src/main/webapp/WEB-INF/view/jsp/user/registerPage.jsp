<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="/WEB-INF/view/jsp/error/errorPage.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/view/taglib/registerForm.tld" prefix="custom" %>
<%@ page import="coffee.machine.view.config.Parameters" %>
<%@ page import="coffee.machine.view.config.Pages" %>
<%@ page import="coffee.machine.view.config.Attributes" %>
<%@ page import="coffee.machine.controller.RegExp" %>

<jsp:include page="${Pages.VIEW_JSP_CLASSPATH}/fragment/header.jsp" />
<br>
<custom:registerForm formTitleMessageKey="${requestScope[Attributes.REGISTER_FORM_TITLE]}"
                  cssClass="register_form"
                  action="${pageContext.request.contextPath}${requestScope[Attributes.REGISTER_FORM_ACTION]}"
                  loginLabelMessageKey="login.email"
                  parameterLogin="${Parameters.LOGIN_PARAM}"
                  loginPreviousValue="${requestScope[Attributes.PREVIOUS_ENTERED_EMAIL]}"
                  passwordLabelMessageKey="login.password"
                  parameterPassword="${Parameters.PASSWORD_PARAM}"
                  fullNameLabelMessageKey="register.full.name"
                  fullNamePreviousValue="${requestScope[Attributes.PREVIOUS_ENTERED_FULL_NAME]}"
                  parameterFullName="${Parameters.FULL_NAME_PARAM}"
                  submitMessageKey="login.submit"
                  cancelMessageKey="cancel"
/>
<br>
<jsp:include page="${Pages.VIEW_JSP_CLASSPATH}/fragment/footer.jsp" />
