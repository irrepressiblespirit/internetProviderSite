<%@ page pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<fmt:setLocale value="${not empty sessionScope.lang ? sessionScope.lang : 'en_US'}" scope="session"/>
<fmt:setBundle basename="ua.nure.skibnev.SummaryTask4.loc.localization"/>
<html>
<c:set var="title" value="Settings Rates" scope="page" />
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<form action="controller" method="post">
<input type="hidden" name="command" value="settingsRates"/>
<table id="main-container">
<tr>
<td class="content">
<c:set var="pageName" value="settingsRates" scope="page" />
<div class="col"><%@ include file="/WEB-INF/jspf/header.jspf" %></div>
<table id="center_table">
<tr><td id="td_border"><fmt:message key="admin.tariff.name"/></td><td id="td_border"><fmt:message key="admin.tariff.price"/></td><td id="td_border"><fmt:message key="admin.tariff.services.list"/></td><td id="td_border"><fmt:message key="admin.tariff.delete"/></td></tr>
<c:forEach var="i" items="${requestScope.tariffs}">
<tr><td id="td_border">${i.getName()}</td><td id="td_border">${i.getPrice()}</td>
<td id="td_border"><c:forEach var="j" items="${i.getServices()}"><c:out value="${j} "></c:out></c:forEach></td><td id="td_border"><input type="submit" name="button" value="${i.getName()}"></td></tr>
</c:forEach>
</table>
<fieldset id="center_table">
<legend class="col"><fmt:message key="admin.tariff.add.edit"/>:</legend>
<table class="col">
<tr><td><fmt:message key="admin.tariff.name"/>:</td><td><input name="name" type="text" value="${requestScope.name}"></td></tr>
<tr><td><fmt:message key="admin.tariff.price"/>:</td><td><input name="price" type="text" value="${requestScope.price}"></td></tr>
</table>
<fmt:message key="admin.tariff.services.list"/>:<br class="col">
<fmt:message key="admin.settings.users.telephone"/><input name="check" type="checkbox" value="Telephone" class="col"><fmt:message key="admin.tariff.service.internet"/><input name="check" type="checkbox" value="Internet" class="col"><fmt:message key="admin.tariff.service.cable"/><input name="check" type="checkbox" value="Cable_tv" class="col"><fmt:message key="admin.tariff.service.iptv"/><input name="check" type="checkbox" value="iptv" class="col">
<br><div id="button_margin"><input type="submit" name="create" value="<fmt:message key="admin.tariff.create.button"/>"><input type="submit" name="edt" value="<fmt:message key="admin.tariff.edit.button"/>"></div>
</fieldset>
<div class="cent"><%@ include file="/WEB-INF/jspf/footer.jspf" %></div>
</td></tr>
</table>
</form>
</body>
</html>