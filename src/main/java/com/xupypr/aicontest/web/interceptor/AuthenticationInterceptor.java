package com.xupypr.aicontest.web.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.mongodb.DBObject;
import com.xupypr.aicontest.database.MongoConnector;

public class AuthenticationInterceptor implements HandlerInterceptor
{
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{
		request.setCharacterEncoding("UTF-8");
		// Если мы не влогинены - сначала посмотрим в кукисы
		if (request.getCookies() != null && request.getSession().getAttribute("sessionkey")==null)
		{
			for (Cookie c : request.getCookies())
			{
				if (c.getName().equals("loginc"))
				{
					request.getSession().setAttribute("sessionkey", c.getValue());
					MongoConnector mc = new MongoConnector();
					DBObject doc = mc.getUserByUID(c.getValue());
					request.getSession().setAttribute("login", doc.get("login"));
				}
			}
		}
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception
	{
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception
	{
	}
}