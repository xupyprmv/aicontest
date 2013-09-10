package com.xupypr.aicontest.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mongodb.DBObject;
import com.xupypr.aicontest.database.MongoConnector;

@Controller
@RequestMapping("/bot")
public class BotController
{

	@RequestMapping(value = "/{action}", method =
	{ RequestMethod.GET, RequestMethod.POST })
	public String doAction(@PathVariable String action, ModelMap model, HttpServletRequest request, HttpServletResponse response)
	{
		MongoConnector mc = new MongoConnector();
		String userId = mc.getUserIdBySessionKey((String) request.getSession().getAttribute("sessionkey"));
		boolean admin = request.getSession().getAttribute("login")!=null && request.getSession().getAttribute("login").equals("admin");
		if (userId != null && !userId.equals(""))
		{
			if (action != null)
			{
				if (action.equals("send"))
				{
					String language = request.getParameter("language") == null ? "" : request.getParameter("language");
					String source = request.getParameter("source") == null ? "" : request.getParameter("source");
					// Проверить заполнение полей		
					if (language.equals(""))
					{
						model.addAttribute("failureMessage",
								"Вы должны выбрать язык/компилятор");
						model.addAttribute("menuActiveItem", "bot");
						return "bot";
					}
					if (source.equals(""))
					{
						model.addAttribute("failureMessage",
								"Вы должны внести код вашего бота");
						model.addAttribute("menuActiveItem", "bot");
						return "bot";
					}
					if (mc.createBot(userId, language, source) != null)
					{
						model.addAttribute("successMessage",
								"Ваш бот сохранён и в ближайшее время приступит к сражению на арене.");
						model.addAttribute("events", mc.getArenaEvents(userId)); 
						model.addAttribute("menuActiveItem", "arena");
						return "arena";
					} else
					{
						model.addAttribute("failureMessage",
								"Ваш бот не сохранён. Обратитесь к жюри.");
						model.addAttribute("menuActiveItem", "bot");
						return "bot";
					}
				} else
				{
					String id = request.getParameter("id") == null ? "" : request.getParameter("id");
					if (id.equals("")) {
						model.addAttribute("failureMessage",
								"Бота не существует.");
						return "source";						
					}
					if (action.equals("view"))
					{
						DBObject bot = mc.getBotByUID(id);
						if (bot != null)
						{
							if (bot.get("user").equals(userId) || admin)
							{
								model.addAttribute("source", bot.get("source"));
								return "source";
							} else
							{
								model.addAttribute("failureMessage",
										"Нельзя просмотреть код чужого бота.");
								return "source";
							}
						} else
						{
							model.addAttribute("failureMessage",
									"Бота не существует.");
							return "source";
						}
					}
					if (action.equals("recompile") && admin)
					{
							mc.recompileBotByUID(id);
							model.addAttribute("successMessage",
									"Бот отправлен на перекомпиляцию.");
							model.addAttribute("bots", mc.getRating()); 
							model.addAttribute("menuActiveItem", "rating");		
							return "rating";
					}
					if (action.equals("delete") && admin)
					{
							mc.deleteBotByUID(id);
							model.addAttribute("successMessage",
									"Бот удалён.");
							model.addAttribute("bots", mc.getRating()); 
							model.addAttribute("menuActiveItem", "rating");		
							return "rating";
					}
				}
			}
			model.addAttribute("menuActiveItem", "bot");
			return "bot";
		} else
		{
			model.addAttribute("failureMessage",
					"Войдите в систему или зарегистрируйтесь, чтобы отправлять ботов.");
			model.addAttribute("menuActiveItem", "registration");
			return "registration";
		}
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String doDefaultAction(ModelMap model, HttpServletRequest request, HttpServletResponse response)
	{
		return doAction(null, model, request, response);
	}
}