<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/view/taglib/loginForm.tld" prefix="custom" %>
<%@ page import="coffee.machine.view.PagesPaths" %>
<%@ page import="coffee.machine.controller.RegExp" %>
<%@ page import="coffee.machine.i18n.message.key.GeneralKey" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="/WEB-INF/view/jsp/fragment/header.jsp" %>
<br>

<div class="register_form">
    <br>
    <b><fmt:message key="register.form.user.title"/></b><br>
    <hr>
    <fmt:message key="error.login.email.do.not.match.pattern" var="titleLoginInput"/>
    <fmt:message key="error.login.password.do.not.match.pattern" var="titlePasswordInput"/>
    <fmt:message key="error.register.full.name.do.not.match.pattern" var="titleFullNameInput"/>
    <fmt:message key="register.submit" var="message_submit"/>
    <fmt:message key="cancel" var="message_cancel"/>
    <form action="${requestScope[Attributes.REGISTER_FORM_ACTION]}" method="post">
        <table>
            <tr>
                <td><br><label for="login"><fmt:message key="login.email"/></label>&nbsp;<br><br></td>
                <td><br><input id="login" minlength="6" type="text" name="${Parameters.LOGIN_PARAM}"
                               value="${requestScope[Attributes.PREVIOUS_ENTERED_EMAIL]}"
                               pattern="${RegExp.REGEXP_EMAIL}" required
                               title="${titleLoginInput}"
                                /><br><br></td>
            </tr>
            <tr>
                <td><br><label for="pswd"><fmt:message key="login.password"/></label>&nbsp;<br><br></td>
                <td><br><input id="pswd" minlength="4" type="password" name="${Parameters.PASSWORD_PARAM}"
                               pattern="${RegExp.REGEXP_PASSWORD}" required
                               title="${titlePasswordInput}"/><br><br></td>
            </tr>
            <tr>
                <td><br><label for="fullName"><fmt:message key="register.full.name"/></label>&nbsp;<br><br></td>
                <td><br><input id="fullName" minlength="3" type="text" name="${Parameters.FULL_NAME_PARAM}"
                               value="${requestScope[Attributes.PREVIOUS_ENTERED_FULL_NAME]}"
                               pattern="${RegExp.REGEXP_FULL_NAME}" required
                               title="${titleFullNameInput}" min="1"/><br><br></td>
            </tr>
            <tr>
                <td colspan="2">

                    <div align="center">
                        <input type="submit" value="${message_submit}">&nbsp;
                        <input type="button" value="${message_cancel}" onclick="history.back()">
                    </div>
                </td>
            </tr>
        </table>
    </form>
    <br>
</div>






<%@ include file="/WEB-INF/view/jsp/fragment/footer.jsp" %>
