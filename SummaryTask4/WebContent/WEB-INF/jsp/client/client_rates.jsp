<%@ page pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ taglib uri="/WEB-INF/mytaglib.tld" prefix="tag" %>
<!DOCTYPE html>

<fmt:setLocale value="${not empty sessionScope.lang ? sessionScope.lang : 'en_US'}" scope="session"/>
<fmt:setBundle basename="ua.nure.skibnev.SummaryTask4.loc.localization"/>
<html>
<c:set var="title" value="Rates" scope="page" />
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>
<form action="controller" method="post">
<input type="hidden" name="command" value="userNewRate"/>
<div id="main-container">
<div class="content">
<c:set var="pageName" value="userRates" scope="page" />
<div class="col"><%@ include file="/WEB-INF/jspf/header.jspf" %></div>
</div>
</div>
<table id="main-container">
<tr>
<td class="content">
<div id="center_table"><fmt:message key="client.select.rate"/>:<c:out value="${requestScope.rate}"></c:out></div>
<table id="center_table">
<tr><td id="td_border"><fmt:message key="admin.tariff.name"/></td><td id="td_border"><fmt:message key="admin.tariff.price"/></td><td id="td_border"><fmt:message key="admin.tariff.services.list"/></td><td id="td_border"><fmt:message key="client.new.rate"/></td></tr>
<c:forEach var="i" items="${requestScope.tariffs}">
<tr><td id="td_border">${i.getName()}</td><td id="td_border">${i.getPrice()}</td>
<td id="td_border"><c:forEach var="j" items="${i.getServices()}"><c:out value="${j} "></c:out></c:forEach></td><td id="td_border"><input type="submit" name="select" value="${i.getName()}"></td></tr>
</c:forEach>
</table>
</td></tr>
</table>
<table>
</table>
</form>
<div class="cent"><%@ include file="/WEB-INF/jspf/footer.jspf" %></div>
</body>
</html>