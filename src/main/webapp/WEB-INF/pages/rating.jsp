<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/WEB-INF/pages/header.jsp" %>
<div id="rating" class="container">
<table class="table table-striped table-hover">
  <thead>
	<tr>
		<th>#</th>
		<th>Имя</th>
		<th>Победа</th>
		<th>Ничья</th>
		<th>Поражение</th>
		<th>Игры</th>
		<th>Очки</th>
	</tr>
  </thead>
  <tbody>
  <c:forEach var="bot" items="${bots}">
	<tr ondblclick="window.location = '${pageContext.request.contextPath}/bot/view?id=${bot._id}'">		
		<td>${bot.position}</td>
		<td>${bot.username}</td>
		<td>${bot.wins}</td>
		<td>${bot.draws}</td>
		<td>${bot.loses}</td>
		<td>${bot.games}</td>
		<td>${bot.points}
		<% if (session.getAttribute("login")!=null && session.getAttribute("login").equals("admin")) { %>
		<a href="${pageContext.request.contextPath}/arena/${bot.user}"><i class="icon-eye-open"></i></a>		
		<a onclick="return confirm('Перекомпилировать бота?')" href="${pageContext.request.contextPath}/bot/recompile?id=${bot._id}"><i class="icon-refresh"></i></a>		
		<a onclick="return confirm('Удалить бота?')" href="${pageContext.request.contextPath}/bot/delete?id=${bot._id}"><i class="icon-remove"></i></a>		
		<%}%>
		</td>
	</tr>
  </c:forEach>
  </tbody>
</table>
</div>
<%@ include file="/WEB-INF/pages/footer.jsp" %>