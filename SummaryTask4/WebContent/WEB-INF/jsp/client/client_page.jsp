<%@ page pageEncoding="UTF-8"%>
    <%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
    <%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
    <%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<fmt:setLocale value="${not empty sessionScope.lang ? sessionScope.lang : 'en_US'}" scope="session"/>
<fmt:setBundle basename="ua.nure.skibnev.SummaryTask4.loc.localization"/>
<html>
<c:set var="title" value="Client page" scope="page" />
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<body>

<form action="controller" method="post">
<input type="hidden" name="command" value="payment"/>
<div id="main-container">
<div class="content">
<c:set var="pageName" value="userStartPage" scope="page" />
<div class="col"><%@ include file="/WEB-INF/jspf/header.jspf" %></div>
</div>
</div>
<table id="main-container">
<tr>
<td class="content">
<table>
<tr><td><h1><fmt:message key="client.page.full.inf"/></h1></td></tr>
<c:set var="i" value="${sessionScope.usr}"></c:set>
<tr><td><fmt:message key="admin.settings.users.firstName"/>:<c:out value="${i.getFirstName()}"></c:out></td></tr>
<tr><td><fmt:message key="admin.settings.users.lastName"/>:<c:out value="${i.getLastName()}"></c:out></td></tr>
<tr><td><fmt:message key="admin.settings.users.telephone"/>:<c:out value="${i.getTelephone()}"></c:out></td></tr>
<tr><td><fmt:message key="admin.settings.users.address"/>:<c:out value="${i.getAddress()}"></c:out></td></tr>
<tr><td><fmt:message key="admin.settings.users.email"/>:<c:out value="${i.getEmail()}"></c:out></td></tr>
<tr><td><fmt:message key="admin.settings.users.account"/>:<c:out value="${i.getCount()}"></c:out></td></tr>
<tr><td><fmt:message key="admin.settings.users.rate"/>:<c:out value="${i.getRates()}"></c:out></td></tr>
</table>
</td></tr>
</table>
<fmt:message key="client.payment"/>:
<input type="text" name="payment">
<input type="submit" name="pay" value="<fmt:message key="client.pay.button"/>"><input type="submit" name="test" value="<fmt:message key="client.test.button"/>">

<div class="cent">
<tags:dates>
<jsp:attribute name="num"><c:out value="${sessionScope.paymentDateInstance.currentdate}"></c:out></jsp:attribute>
<jsp:body><c:out value="${sessionScope.paymentDateInstance.paymentdate}"></c:out></jsp:body>
</tags:dates>
</div>
<div class="cent"><%@ include file="/WEB-INF/jspf/footer.jspf" %></div>
</form>
</body>
</html>