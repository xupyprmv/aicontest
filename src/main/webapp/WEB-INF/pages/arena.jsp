<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/WEB-INF/pages/header.jsp" %>
<div class="container">
<table class="table table-striped table-hover">
 <thead>
	<tr>
		<th>Дата / время</th>
		<th>Событие</th>
		<th>Журнал игры</th>
	</tr>
 </thead>
 <tbody>
  <c:forEach var="event" items="${events}">
	<tr ondblclick="window.location = '${pageContext.request.contextPath}/arena/view?id=${event._id}'" class="
		${event.type=="created"?"info":""}
		${event.type=="compile-success" || (event.type=="win" && event.bot1==mybot) || (event.type=="loss" && event.bot2==mybot)?"success":""}
		${event.type=="compile-failure" || (event.type=="loss" && event.bot1==mybot) || (event.type=="win" && event.bot2==mybot)?"error":""}
		">		
		<td>${event.sdate}</td>
		<td>${event.comment}</td>
		<td>${event.game}</td>
	</tr>
  </c:forEach>
  </tbody>
</table>
<script type="text/javascript">

var selected = null;

$(document).ready(function(){
   $("#rating table tbody tr").dblclick(function(){
      window.location = "${pageContext.request.contextPath}/bot/view?id=" + $(this).prop("uid");
   });
});

</script></div>
<%@ include file="/WEB-INF/pages/footer.jsp" %>