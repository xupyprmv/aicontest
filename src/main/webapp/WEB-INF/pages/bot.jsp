<%@ page pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/header.jsp"%>
<div class="container">
	<div class="well">
		<form id="sendbot" class="form-horizontal" method="post"
			action="${pageContext.request.contextPath}/bot/send">
			<legend>Отправить бота</legend>
			<div class="row">
				<div class="span11">
					<h5>Выберите язык</h5>
				</div>
			</div>
			<div class="row">
				<div class="input-prepend span11">
					<span class="add-on"><i class="icon-wrench"></i></span> 
						<select id="language" name="language">
							<option value="PASCAL">Pascal / FPC 2.4.4</option>
							<option value="JAVA">Java / OpenJDK 1.6.0.24</option>
							<option value="CPP">C++ / GNU C++ 4.6.3</option>
							<option value="PYTHON">Python / Python 2.7.3</option>
						</select>							
				</div>
			</div>
			<div class="row">
				<div class="span11">
					<h5>Исходный код</h5>
				</div>
			</div>
			<div class="row">
				<div class="input-prepend span11">
					<span class="add-on"><i class="icon-pencil"></i></span> 
					<textarea id="source" name="source" class="field span11" rows="30"></textarea>
				</div>
			</div>
			<div class="row">
				<div class="span11" style="text-align: center;">
					<button type="submit" class="btn btn-success" style="margin:25px;">Отправить</button>
				</div>
			</div>
		</form>

	</div>
</div>
<%@ include file="/WEB-INF/pages/footer.jsp"%>