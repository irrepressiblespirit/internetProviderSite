
    <%@ tag import="ua.nure.skibnev.SummaryTask4.web.tags.PaymentDate" %>
    <%@ attribute name="num" fragment="true" %>
    <%@ variable name-given="paymentDateInstance" %>
    <%session.setAttribute("paymentDateInstance", new PaymentDate());%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<TABLE>
<TR><TD><jsp:invoke fragment="num"></jsp:invoke></TD></TR>
	<TR><TD><jsp:doBody /></TD></TR>
</TABLE>
</body>
</html>