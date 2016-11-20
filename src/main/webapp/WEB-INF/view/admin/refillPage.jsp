<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="static coffee_machine.controller.PagesPaths.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Insert title here</title>
</head>
<body>
<%@ include file="/WEB-INF/view/fragment/header.jsp" %>


<fmt:message key="admin.refill.description"/> <br>

<form action="<%=ADMIN_REFILL_SUBMIT_PATH%>" method="post">

    <table cellpadding="10" cellspacing="2" border="1">
        <tr>

            <td>
                <div align="center"><fmt:message key="refill.goods.name"/></div>
            </td>
            <td>
                <div align="center"><fmt:message key="refill.goods.available"/></div>
            </td>
            <td>
                <div align="center"><fmt:message key="refill.goods.add.quantity"/></div>
            </td>
        </tr>


        <tr>
            <td colspan="3">
                <div align="center"><fmt:message key="refill.base.drinks.title"/></div>
            </td>
        </tr>
        <c:set var="drinks_attr" value="<%=Attributes.REFILL_DRINKS%>"/>
        <c:forEach items="${requestScope[drinks_attr]}" var="drink">
            <tr>
                <td>${drink.name}</td>
                <td>
                    <div align="center">${drink.quantity}</div>
                </td>
                <td>
                    <div align="center">
                        <input type="text" value="0" name="<%=Parameters.DRINK_PARAMETER_STARTS_WITH%>${drink.id}"
                               style="width: 20pt">
                    </div>
                </td>
            </tr>
        </c:forEach>

        <tr>
            <td colspan="3">
                <div align="center"><fmt:message key="refill.addons.title"/></div>
            </td>
        </tr>
        <c:set var="addons_attr" value="<%=Attributes.REFILL_ADDONS%>"/>
        <c:forEach items="${requestScope[addons_attr]}" var="addon">
            <tr>
                <td>${addon.name}</td>
                <td>
                    <div align="center">${addon.quantity}</div>
                </td>
                <td>
                    <div align="center">
                        <input type="text" value="0" name="<%=Parameters.ADDON_PARAMETER_STARTS_WITH%>${addon.id}"
                               style="width: 20pt">
                    </div>
                </td>
            </tr>
        </c:forEach>
        <tr>
            <td colspan="3">
                <br>
                <fmt:message key="refill.submit" var="mes_submit"/>
                <div align="center"><input type="submit" value="${mes_submit}"></div>
                <br>
            </td>
        </tr>
    </table>



    <br>
</form>
<br> <a href="<%=HOME_PATH%>"><fmt:message key="home.page"/></a>
<br>
<br> <a href="<%=USER_LOGOUT_PATH%>"><fmt:message key="logout"/></a>

<%@ include file="/WEB-INF/view/fragment/footer.jsp" %>
</body>
</html>