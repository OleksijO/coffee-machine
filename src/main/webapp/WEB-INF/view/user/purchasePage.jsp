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
<%@ include file="/WEB-INF/view/fragment/header.jsp"%>
			<fmt:message key="user.purchase.description"/> <br>
<br>
<form action="<%=USER_PURCHASE_SUBMIT_PATH%>" method="post">
	<fmt:message key="user.purchase.your.balance.is"/> <fmt:formatNumber value="${user_balance}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
	<fmt:message key="currency"/>.<br>
	<br>
	<table cellpadding="10" cellspacing="2" border="1">
		<tr>

			<td>
				<div align="center"><fmt:message key="purchase.drink.name"/></div>
			</td>
			<td>
				<div align="center"><fmt:message key="purchase.drink.price"/>, <fmt:message key="currency"/></div>
			</td>
			<td>
				<div align="center"><fmt:message key="purchase.drink.available"/></div>
			</td>
			<td>
				<div align="center"><fmt:message key="purchase.drink.quantity"/></div>
			</td>
		</tr>

		<c:set var="drinks_attr" value="<%=Attributes.REFILL_DRINKS%>"/>
		<c:forEach items="${requestScope[drinks_attr]}" var="drink">
			<tr>
				<td><b>${drink.name}</b><br>

				<c:set var="addons" value="${drink.addons}"/>

					<c:forEach items="${addons}" var="addon">
						<c:if test='${addon.quantity>0}'>
						[
						${addon.name} (addon.quantity) - <fmt:formatNumber value="${addon.realPrice}" type="number"
																		   minFractionDigits="2" maxFractionDigits="2"/>
							<fmt:message key="currency"/>

						<select name="<%=Parameters.DRINK_PARAMETER_STARTS_WITH%>${drink.id}
								<%=Parameters.ADDON_PARAMETER_STARTS_WITH%>${addon.id}"
								style="width: 30pt">
							<option selected value="0">0</option>
							<option value="1">1</option>
							<option value="2">2</option>
						</select>
						]
						&nbsp
						</c:if>
					</c:forEach>

				</td>
				<td>
					<div align="center"><fmt:formatNumber value="${drink.realPrice}" type="number"
														  minFractionDigits="2" maxFractionDigits="2"/></div>
				</td>
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
			<td colspan="4">
				<br>
				<fmt:message key="user.purchase.submit" var="mes_submit"/>
				<div align="center"><input type="submit" value="${mes_submit}"></div>
				<br>
			</td>
		</tr>
	</table>

	<br>
</form>

<%@ include file="/WEB-INF/view/fragment/footer.jsp"%>
</body>
</html>