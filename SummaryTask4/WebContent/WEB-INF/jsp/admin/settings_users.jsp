<%@ page pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<fmt:setLocale value="${not empty sessionScope.lang ? sessionScope.lang : 'en_US'}" scope="session"/>
<fmt:setBundle basename="ua.nure.skibnev.SummaryTask4.loc.localization"/>
<html>
<c:set var="title" value="Settings Users" scope="page" />
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<head>
<script type="text/javascript" src="registr.js"></script>
</head>
<body>
<form action="controller" method="post">
<input type="hidden" name="command" value="settingsUsers"/>
<div id="main-container">
<div class="content">
<c:set var="pageName" value="settingsUsers" scope="page" />
<div class="col"><%@ include file="/WEB-INF/jspf/header.jspf" %></div>
</div>
</div>
<table id="main-container">
<tr>
<td class="content">
<table>
<tr><td id="td_border"><fmt:message key="admin.settings.users.role"/>:</td><td id="td_border">
<select name="role" class="col">
<option value="admin"><fmt:message key="admin.settings.users.role.admin"/></option>
<option value="subscriber"><fmt:message key="admin.settings.users.role.subscriber"/></option>
</select></td></tr>
<tr><td id="td_border"><fmt:message key="admin.settings.users.login"/>:</td><td id="td_border"><input name="log" type="text" class="col"></td></tr>
<tr><td id="td_border"><fmt:message key="admin.settings.users.password"/>:</td><td id="td_border"><input name="pass" type="text" class="col"></td></tr>
<tr><td id="td_border"><fmt:message key="admin.settings.users.firstName"/>:</td><td id="td_border"><input name="firstName" type="text" class="col"></td></tr>
<tr><td id="td_border"><fmt:message key="admin.settings.users.lastName"/>:</td><td id="td_border"><input name="lastName" type="text" class="col"></td></tr>
<tr><td id="td_border"><fmt:message key="admin.settings.users.telephone"/>:</td><td id="td_border"><input name="tel" type="text" class="col"></td></tr>
<tr><td id="td_border"><fmt:message key="admin.settings.users.address"/>:</td><td id="td_border"><input name="address" type="text" class="col"></td></tr>
<tr><td id="td_border"><fmt:message key="admin.settings.users.email"/>:</td><td id="td_border"><input name="email" type="text" class="col"></td></tr>
<tr><td id="td_border"><fmt:message key="admin.settings.users.rate"/>:</td><td id="td_border">
<c:forEach var="i" items="${applicationScope.rates}">
<input type="radio" name="rates" value="${i}"/>${i}<br>
</c:forEach>
<tr><td id="td_border"><fmt:message key="admin.settings.users.account"/>:</td><td id="td_border"><input name="balance" type="text" class="col"></td></tr>
<tr><td id="td_border"><fmt:message key="admin.settings.users.statuses"/>:</td><td id="td_border"><select name="stat" class="col">
<option value="blocked"><fmt:message key="admin.settings.users.statuses.block"/></option>
<option value="unblocked"><fmt:message key="admin.settings.users.statuses.unblock"/></option>
</select></td></tr>
</table>
<input type="submit" name="registr" onclick="validate(this.form)" value="<fmt:message key="admin.settings.users.registration.button"/>">
<input type="submit" name="delete" value="<fmt:message key="admin.settings.users.block/unblock.button"/>">
</td></tr>
</table>
</form>
<div id="main-container">
<div class="content">
<h1><fmt:message key="admin.settings.users.information"/>:</h1>
<c:set var="m" value="${requestScope.user}"></c:set>
<c:if test="${!empty m}">
<c:out value="${m}"></c:out>
</c:if>
<c:set var="k" value="${applicationScope.usernewRate}"></c:set>
<c:set var="r" value="${applicationScope.newRate}"></c:set>
<c:if test="${!empty k}">
<c:out value="user ${k} need in new rate ${r}"></c:out>
</c:if>
<div class="cent"><%@ include file="/WEB-INF/jspf/footer.jspf" %></div>
</div>
</div>
</body>
</html>