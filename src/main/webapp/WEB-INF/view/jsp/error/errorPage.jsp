<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
<%@ page import="coffee.machine.view.Attributes" %>
<%@ page import="coffee.machine.view.ErrorMessage" %>
<%@ page import="org.apache.log4j.Logger" %>
<%! static final Logger logger = Logger.getLogger("JSP ERROR HANDLER PAGE"); %>
<% logger.error(String
        .format(ErrorMessage.JSP_HANDLER_MESSAGE_FORMAT,
                pageContext.getSession().getAttribute(Attributes.USER_ROLE),
                pageContext.getSession().getAttribute(Attributes.USER_ID),
                pageContext.getErrorData().getStatusCode(),
                pageContext.getErrorData().getServletName(),
                pageContext.getException().getMessage()), pageContext.getException()); %>
<!DOCTYPE html>
<html>
<head>
    <title>ERROR / ОШИБКА / ПОХИБКА</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<div align="center">
    <div align="center" style="max-width: 1024px">
        <br><br><br><br><br><br>
        <h3><b> EN: An internal error has been occurred. Please, contact your application support team. </b></h3>
        <br><br>
        <h3><b> RU: Произошла внутренняя ошибка. Пожалуйста, обратитесь в службу поддержки вашего приложения. </b></h3>
        <br><br>
        <h3><b> UA: Сталася внутрішня помилка. Будь ласка, зверніться в службу підтримки вашого додатку. </b></h3>
        <br><br>
        <h1><b> support@some.domain.com / 0-800-00-00 </b></h1>

    </div>
</div>

</body>
</html>