<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="coffee.machine.view.config.Paths" %>
<%@ page import="coffee.machine.view.config.Pages" %>
<%@ page import="coffee.machine.view.config.Attributes" %>
<%@ page import="coffee.machine.view.config.Parameters" %>
<jsp:include page="${Pages.VIEW_JSP_CLASSPATH}/fragment/header.jsp" />
<br>
<fmt:message key="admin.add.credit.description"/> <br><br>
<fmt:message key="admin.refill.machine.balance"/> :
<fmt:formatNumber value="${requestScope[Attributes.COFFEE_MACHINE_BALANCE]}"
                  type="number"
                  minFractionDigits="2"
                  maxFractionDigits="2"/>
<fmt:message key="currency"/><br><br>



    <table cellpadding="10" cellspacing="2" border="1">
        <tr>
            <td>
                <div align="center"><b><fmt:message key="admin.add.credit.user.full.name"/></b></div>
            </td>
            <td>
                <div align="center"><b><fmt:message key="login.email"/></b></div>
            </td>
            <td>
                <div align="center"><b><fmt:message key="admin.add.credit.user.balance"/>,
                    <fmt:message key="currency"/></b></div>
            </td>
            <td>
                <div align="center"><b><fmt:message key="admin.add.credit.amount.to.add"/>,
                    <fmt:message key="currency"/></b></div>
            </td>
        </tr>


        <c:forEach items="${requestScope[Attributes.USER_LIST]}" var="user">
            <tr>

                <td>${user.fullName}</td>
                <td>${user.email}</td>
                <td>
                    <div align="center"><fmt:formatNumber value="${user.account.realAmount}"
                                                          type="number"
                                                          minFractionDigits="2"
                                                          maxFractionDigits="2"/>
                    </div>
                </td>
                <td>

                    <form action="${pageContext.request.contextPath}${Paths.ADMIN_ADD_CREDITS_PATH}" method="post">
                        <div align="center">
                            <input type="hidden" name="${Parameters.USER_ID}" value="${user.id}">
                            <input type="number" min="0.01" value="0" name="${Parameters.CREDITS_TO_ADD}"
                                   style="width: 50pt" step="0.01">
                            <fmt:message key="admin.add.credit.submit" var="message_submit"/>
                            <input type="submit" value="${message_submit}">
                        </div>
                    </form>
                </td>

            </tr>
        </c:forEach>


    </table>


    <br>



<jsp:include page="${Pages.VIEW_JSP_CLASSPATH}/fragment/footer.jsp" />
