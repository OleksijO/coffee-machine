<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/view/taglib/loginForm.tld" prefix="custom"%>
<%@ page import="coffee_machine.view.PagesPaths" %>
<%@ page import="coffee_machine.i18n.message.key.GeneralKey" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="/WEB-INF/view/jsp/fragment/header.jsp" %>

<custom:loginForm formTitleMessageKey="${requestScope[Attributes.LOGIN_FORM_TITLE]}"
                  cssClass="login_form"
                  action="${requestScope[Attributes.LOGIN_FORM_ACTION]}"
                  loginLabelMessageKey="login.email"
                  parameterLogin="${Parameters.LOGIN}"
                  loginPreviosValue="${sessionScope[Attributes.PREVIOUS_ENTERED_EMAIL]}"
                  passwordLabelMessageKey="login.password"
                  parameterPassword="${Parameters.PASSWORD}"
                  submitMessageKey="login.submit"
/>

<%@ include file="/WEB-INF/view/jsp/fragment/footer.jsp" %>
