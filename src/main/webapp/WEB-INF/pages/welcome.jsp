<%@ page pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/header.jsp" %>
<div class="container">
	<div class="row">
		<div class="span8">
			<img alt="Author: http://kitajec.deviantart.com/"
				src="${pageContext.request.contextPath}/img/logo.jpg" />
		</div>
		<div class="span4">
			<h3><p class="text-info">Коми республиканский физико-математический лицей-интернат</p></h3>
			<h2>Добро пожаловать на AI Contest!!!</h2>
			Сегодня вам будет предложено создать искуственный интеллект -
			компьютерных игроков, которые будут сражаться друг с другом на
			виртуальной арене. И пусть победит сильнейший! 
			<br /><br /> 
			<b>P. S.</b> Перед началом участия обязательно, 
			<a href="${pageContext.request.contextPath}/registration/new">зарегистрируйтесь</a> и ознакомтесь с разделом <a href="/FAQ">FAQ</a>. <br />
			<br /> <a class="btn btn-large btn-success" href="${pageContext.request.contextPath}/registration/new">Я готов <i class="icon-fire icon-white"></i></a>
		</div>
	</div>
</div>
<%@ include file="/WEB-INF/pages/footer.jsp" %>