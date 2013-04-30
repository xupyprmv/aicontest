<%@ page pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/header.jsp"%>
<div class="container">
	<div class="well">
		<form id="signup" class="form-horizontal" method="post"
			action="${pageContext.request.contextPath}/registration/save">
			<legend>Регистрация</legend>
			<div class="control-group ">
				<label class="control-label">Фамилия и имя</label>
				<div class="controls">
					<div class="input-prepend">
						<span class="add-on"><i class="icon-user"></i></span> <input
							type="text" class="input-xlarge" id="uname" name="uname"
							placeholder="Фамилия и имя" value="${uname}">							
					</div>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Класс</label>
				<div class="controls">
					<script type="text/javascript">
					$(document).ready(function(){
							<%
							for (int i = 7; i < 12; i++) {
							%>
							$('#grade<%=i%>').click(function() {
						    	$('input[name=grade]').val(<%=i%>);
							});
							<% } %>
					});
					</script>
					<input type="hidden" id="grade" name="grade" value="${grade}">
					<div id="gradebox" name="gradebox" class="btn-group" data-toggle="buttons-radio">
						<button id="grade7" type="button" class="btn btn-info ${grade=='7'?'active':''}">7</button>
						<button id="grade8" type="button" class="btn btn-info ${grade=='8'?'active':''}">8</button>
						<button id="grade9" type="button" class="btn btn-info ${grade=='9'?'active':''}">9</button>
						<button id="grade10" type="button" class="btn btn-info ${grade=='10'?'active':''}">10</button>
						<button id="grade11" type="button" class="btn btn-info ${grade=='11'?'active':''}">11</button>
					</div>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Логин</label>
				<div class="controls">
					<div class="input-prepend">
						<span class="add-on"><i class="icon-user"></i></span> <input
							type="text" class="input-xlarge" id="login" name="login"
							placeholder="Логин" value="${login}">
					</div>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Пароль</label>
				<div class="controls">
					<div class="input-prepend">
						<span class="add-on"><i class="icon-lock"></i></span> <input
							type="Password" id="passwd" class="input-xlarge" name="passwd"
							placeholder="Password">
					</div>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Подтверждение пароля</label>
				<div class="controls">
					<div class="input-prepend">
						<span class="add-on"><i class="icon-lock"></i></span> <input
							type="Password" id="conpasswd" class="input-xlarge"
							name="conpasswd" placeholder="Re-enter Password">
					</div>
				</div>
			</div>

			<div class="control-group">
				<label class="control-label"></label>
				<div class="controls">
					<button type="submit" class="btn btn-success">Зарегистрироваться</button>
				</div>

			</div>

		</form>

	</div>
</div>
<%@ include file="/WEB-INF/pages/footer.jsp"%>