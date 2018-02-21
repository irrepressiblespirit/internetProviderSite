<%@ page pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<fmt:setLocale value="${not empty sessionScope.lang ? sessionScope.lang : 'en_US'}" scope="session"/>
<fmt:setBundle basename="ua.nure.skibnev.SummaryTask4.loc.localization"/>
<html>
<c:set var="title" value="Users Info" scope="page" />
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>
<form action="controller" method="post">
<input type="hidden" name="command" value="blockUnblockUsers"/>
<c:set var="pageName" value="blockUnblockUsers" scope="page" />
<div class="col"><%@ include file="/WEB-INF/jspf/header.jspf" %></div>
<table id="main-container">
<tr>
<td class="content">
<table id="center_table">
<tr><td><h1><fmt:message key="admin.find.user"/>:</h1></td><td><input type="radio" name="choise" value="log" checked="checked"/><fmt:message key="admin.settings.users.login"/><br><input type="radio" name="choise" value="lastName"/><fmt:message key="admin.settings.users.lastName"/><br></td><td><input type="text" name="data"/></td><td><input type="submit" name="fnd" value="<fmt:message key="admin.find.button"/>"></td></tr>
</table>
<br>
<table id="mrg">
<tr><td id="td_border"><fmt:message key="admin.settings.users.login"/></td><td id="td_border"><fmt:message key="admin.settings.users.password"/></td><td id="td_border"><fmt:message key="admin.settings.users.firstName"/></td><td id="td_border"><fmt:message key="admin.settings.users.lastName"/></td><td id="td_border"><fmt:message key="admin.settings.users.telephone"/></td><td id="td_border"><fmt:message key="admin.settings.users.address"/></td><td id="td_border"><fmt:message key="admin.settings.users.email"/></td><td id="td_border"><fmt:message key="admin.settings.users.account"/></td><td id="td_border"><fmt:message key="admin.settings.users.statuses"/></td><td id="td_border"><fmt:message key="admin.settings.users.rate"/></td><td id="td_border"><fmt:message key="admin.settings.users.role"/></td><td id="td_border"><fmt:message key="admin.setting.status"/></td></tr>
<c:set var="j" value="${requestScope.userCount}"></c:set>
<c:choose>
<c:when test="${j==1}">
<c:set var="k" value="${requestScope.users}"></c:set>
<tr><td id="td_border"><c:out value="${k.getLogin()}"></c:out></td><td id="td_border"><c:out value="${k.getPassword()}"></c:out></td><td id="td_border"><c:out value="${k.getFirstName()}"></c:out></td><td id="td_border"><c:out value="${k.getLastName()}"></c:out></td><td id="td_border"><c:out value="${k.getTelephone()}"></c:out></td><td id="td_border"><c:out value="${k.getAddress()}"></c:out></td><td id="td_border"><c:out value="${k.getEmail()}"></c:out></td><td id="td_border"><c:out value="${k.getCount()}"></c:out></td><td id="td_border"><c:out value="${k.getStatus()}"></c:out></td><td id="td_border"><c:out value="${k.getRates()}"></c:out></td><td id="td_border"><c:out value="${k.getRole()}"></c:out></td><td id="td_border"><input type="submit" name="blk" value="${k.getLogin()}"></td></tr>
</c:when>
<c:otherwise>
<c:forEach var="i" items="${requestScope.users}">
<tr><td id="td_border"><c:out value="${i.getLogin()}"></c:out></td><td id="td_border"><c:out value="${i.getPassword()}"></c:out></td><td id="td_border"><c:out value="${i.getFirstName()}"></c:out></td><td id="td_border"><c:out value="${i.getLastName()}"></c:out></td><td id="td_border"><c:out value="${i.getTelephone()}"></c:out></td><td id="td_border"><c:out value="${i.getAddress()}"></c:out></td><td id="td_border"><c:out value="${i.getEmail()}"></c:out></td><td id="td_border"><c:out value="${i.getCount()}"></c:out></td><td id="td_border"><c:out value="${i.getStatus()}"></c:out></td><td id="td_border"><c:out value="${i.getRates()}"></c:out></td><td id="td_border"><c:out value="${i.getRole()}"></c:out></td><td id="td_border"><input type="submit" name="blk" value="${i.getLogin()}"></td></tr>
</c:forEach>
</c:otherwise>
</c:choose>
</table>
</td></tr>
</table>
</form>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>