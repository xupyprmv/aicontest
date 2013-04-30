package com.xupypr.aicontest.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value =
{ "/", "/dvonn", "/welcome", "/FAQ"})
public class DefaultController
{

	@RequestMapping(value =
	{ "/" }, method = RequestMethod.GET)
	public String doDefaultAction(Model model, HttpServletRequest request) throws Exception
	{
		String whatYouCallValue = request.getServletPath();
		if (whatYouCallValue.equals("") || whatYouCallValue.equals("/"))
		{
			model.addAttribute("menuActiveItem", "welcome");
			return "welcome";
		} else
		{
			String result = whatYouCallValue.replaceAll("/", "");
			model.addAttribute("menuActiveItem", result);
			return result;
		}
	}
}