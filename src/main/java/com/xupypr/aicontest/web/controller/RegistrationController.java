package com.xupypr.aicontest.web.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xupypr.aicontest.database.MongoConnector;

@Controller
@RequestMapping("/registration")
public class RegistrationController
{

	@RequestMapping(value = "/{action}", method =
	{ RequestMethod.GET, RequestMethod.POST })
	public String doAction(@PathVariable String action, ModelMap model, HttpServletRequest request, HttpServletResponse response)
	{
		if (action != null)
		{
			if (action.equals("save"))
			{
				return doSave(model, request);
			}
			if (action.equals("login"))
			{
				return doLogin(model, request, response);
			}
			if (action.equals("logout"))
			{
				return doLogout(model, request, response);
			}
		}

		if (request.getSession().getAttribute("logins")!=null && !request.getSession().getAttribute("logins").equals("")) {
			// Если уже залогинились - переходим на форму отправки
			model.addAttribute("menuActiveItem", "bot");
			return "bot";			
		} else {
			// Если ещё не залогинились - на форму регистрации
			model.addAttribute("menuActiveItem", "registration");
			return "registration";
		}
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String doDefaultAction(ModelMap model, HttpServletRequest request, HttpServletResponse response)
	{
		return doAction(null, model, request, response);
	}

	private String doLogin(ModelMap model, HttpServletRequest request, HttpServletResponse response)
	{
		String login = request.getParameter("login")==null?"":request.getParameter("login");
		String passwd = request.getParameter("passwd")==null?"":request.getParameter("passwd");
		String rememberme = request.getParameter("rememberme")==null?"":request.getParameter("rememberme");
		MongoConnector mc = new MongoConnector();

		// Проверить что есть запись логин - пароль в базе данных
		String userUID = mc.loginUser(login, passwd);
		if (userUID!=null)
		{
			model.addAttribute("login", login);
			request.getSession().setAttribute("logins", userUID);
			request.getSession().setAttribute("login", login);
			if (rememberme!=null && rememberme.equals("on")) {
				Cookie cookie = new Cookie("loginc", userUID);
				cookie.setMaxAge(60*60*24);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
			model.addAttribute("menuActiveItem", "dvonn");
			return "dvonn";
		} else
		{
			if (mc.checkUserExistsByLogin(login))
			{
				// Если логин совпал - значит неверный пароль
				model.addAttribute("failureMessage", "Неправильный пароль");
				model.addAttribute("menuActiveItem", "welcome");
				return "welcome";
			} else
			{
				// Нету логина в базе
				model.addAttribute("failureMessage",
						"Данной комбинации логина / пароля нет в системе. Возможно, вам нужно зарегистрироваться");
				model.addAttribute("menuActiveItem", "registration");
				return "registration";
			}
		}
		// Если нет
	}

	private String doLogout(ModelMap model, HttpServletRequest request, HttpServletResponse response)
	{
		request.getSession().setAttribute("logins", "");
		request.getSession().setAttribute("login", "");
		Cookie cookie = new Cookie("loginc", null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
		return "welcome";
	}

	private String doSave(ModelMap model, HttpServletRequest request)
	{
		String uname = request.getParameter("uname")==null?"":request.getParameter("uname");
		String grade = request.getParameter("grade")==null?"":request.getParameter("grade");
		String login = request.getParameter("login")==null?"":request.getParameter("login");
		String passwd = request.getParameter("passwd")==null?"":request.getParameter("passwd");
		String conpasswd = request.getParameter("conpasswd")==null?"":request.getParameter("conpasswd");

		// Запоминаем полученные значения
		model.addAttribute("uname", uname);
		model.addAttribute("grade", grade);
		model.addAttribute("login", login);

		// Проверить заполнение всех полей		
		if (uname.equals("")) {
			model.addAttribute("failureMessage",
					"Поле `Фамилия и имя` должно быть заполнено");
			return "registration";
		}
		if (grade.equals("")) {
			model.addAttribute("failureMessage",
					"Поле `Класс` должно быть заполнено");
			return "registration";
		}
		if (login.equals("")) {
			model.addAttribute("failureMessage",
					"Поле `Логин` должно быть заполнено");
			return "registration";
		}
		if (passwd.equals("")) {
			model.addAttribute("failureMessage",
					"Поле `Пароль` должно быть заполнено");
			return "registration";
		}
		// Сверить совпадение пароля и подтверждения
		if (!passwd.equals(conpasswd)) {
			model.addAttribute("failureMessage",
					"Значения полей `Пароль` и `Подтверждение пароля` должны совпадать");
			return "registration";
		}
		// Сверить уникальность логина
		MongoConnector mc = new MongoConnector();
		if (mc.checkUserExistsByLogin(login)) {
			model.addAttribute("failureMessage",
					"Пользователь с данным логином уже существует");
			return "registration";
		}
		
		// Если регистрация успешна
		// 1. Сохраняем пользователя и автоматически влогиниваем его
		request.getSession().setAttribute("logins", mc.createUser(uname, grade, login, passwd));
		request.getSession().setAttribute("login", login);
		// 2. Выдаём сообщение и отправляем на правила игры 
		model.addAttribute("successMessage", "Регистрация прошла успешно");
		model.addAttribute("menuActiveItem", "dvonn");
		return "dvonn";
	}
}