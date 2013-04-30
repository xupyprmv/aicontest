<%@ page pageEncoding="UTF-8"%>
	<div class="navbar navbar-inverse" style="position: static;">
		<div class="navbar-inner">
			<div class="container">
				<div class="nav-collapse collapse navbar-inverse-collapse">
					<ul class="nav">
<!-- TODO: active menu item -->					
						<li class="${menuActiveItem=="welcome"?"active":""}"><a href="${pageContext.request.contextPath}"><i class="icon-home icon-white"></i></a></li>
						<li class="${menuActiveItem=="dvonn"?"active":""}"><a href="${pageContext.request.contextPath}/dvonn/">Текущее соревнование</a></li>
						<li class="${menuActiveItem=="bot"?"active":""}"><a href="${pageContext.request.contextPath}/bot/">Отправить решения</a></li>
						<li class="${menuActiveItem=="arena"?"active":""}"><a href="${pageContext.request.contextPath}/arena/">Арена</a></li>
						<li class="${menuActiveItem=="rating"?"active":""}"><a href="${pageContext.request.contextPath}/rating/">Рейтинг</a></li>
						<li class="${menuActiveItem=="FAQ"?"active":""}"><a href="${pageContext.request.contextPath}/FAQ/">FAQ</a></li>
						<!-- 
						<li class="dropdown">
							<a href="#" class="dropdown-toggle" data-toggle="dropdown">
								Архив <b class="caret"></b>
							</a>
							<ul class="dropdown-menu">
								<li><a href="#">Action</a></li>
								<li><a href="#">Another action</a></li>
								<li><a href="#">Something else here</a></li>
								<li class="divider"></li>
								<li class="nav-header">Nav header</li>
								<li><a href="#">Separated link</a></li>
								<li><a href="#">One more separated link</a></li>
							</ul>
						</li>
						 -->
					</ul>
					<ul class="nav pull-right">
					<% if (session.getAttribute("logins")!=null && !session.getAttribute("logins").equals("")) { %>
						<li>
							<a href="#"><i class="icon-user icon-white"></i>&nbsp;&nbsp;${login}</a> 
						</li>
						<li>
							<form class="form-inline" style="margin-left:10px; margin-bottom: 0px; margin-top: 4px;" action="${pageContext.request.contextPath}/registration/logout" method="post">
								<button type="submit" class="btn btn-inverse" style="margin-top: 0px;">Выход</button>
							</form>
						</li>
					<% } else { %>
						<li>
							<form class="form-inline" style="margin-bottom: 0px; margin-top: 4px;" action="${pageContext.request.contextPath}/registration/login" method="post">
  								<input id="login" name="login" type="text" class="input-small" placeholder="Логин" />
  								<input id="passwd" name="passwd" type="password" class="input-small" placeholder="Пароль" />
  								<label class="checkbox" style="color:grey;">
    								<input id="rememberme" name="rememberme" type="checkbox" /> Запомнить меня
  								</label>
  								<button type="submit" class="btn btn-success" style="margin-top: 0px;">Войти</button>  								
							</form>
						</li>
						<li>
							<form class="form-inline" style="margin-left:10px; margin-bottom: 0px; margin-top: 4px;" action="${pageContext.request.contextPath}/registration/new" method="post">
									<button type="submit" class="btn btn-warning" style="margin-top: 0px;">Регистрация</button>
  							</form>
						</li>
					<% } %>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<div class="container" style="display: ${(failureMessage!=null && failureMessage!="")?"block":"none"}">
		<div class="alert alert-error">${failureMessage}</div>
	</div>
	<div class="container" style="display: ${(successMessage!=null && successMessage!="")?"block":"none"}">
		<div class="alert alert-success">${successMessage}</div>
	</div>

	