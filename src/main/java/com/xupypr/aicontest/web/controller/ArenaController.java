package com.xupypr.aicontest.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mongodb.DBObject;
import com.xupypr.aicontest.database.MongoConnector;

@Controller
@RequestMapping("/arena")
public class ArenaController
{

	@RequestMapping(value = "/{action}", method = RequestMethod.GET)
	public String doAction(@PathVariable String action, ModelMap model, HttpServletRequest request)
	{
		MongoConnector mc = new MongoConnector();
		String userId = mc.getUserIdBySessionKey((String) request.getSession().getAttribute("sessionkey"));
		if (userId != null && !userId.equals(""))
		{
			boolean admin = request.getSession().getAttribute("login") != null
					&& ((String) request.getSession().getAttribute("login")).equals("admin");
			if (admin)
			{
				String id = request.getParameter("id") == null ? "" : request.getParameter("id");
				if (action!=null && action.equals("view") && !id.equals("")) {
					model.addAttribute("fight", mc.getBotFight(id));
					return "fight";						
				}
				
				if (action == null || action.equals(""))
				{
					model.addAttribute("events", mc.getAllArenaEvents());
				} else
				{
					model.addAttribute("mybot", mc.getBotByUserUID(action).get("_id").toString());
					model.addAttribute("events", mc.getArenaEvents(action));
				}
			} else
			{
				DBObject bot = mc.getBotByUserUID(userId);
				
				if (bot!=null) {					
					model.addAttribute("mybot", bot.get("_id").toString());
				}
				
				String id = request.getParameter("id") == null ? "" : request.getParameter("id");
				if (action!=null && action.equals("view") && !id.equals("") && bot.get("user").toString().equals(userId)) {
					model.addAttribute("game", mc.getBotFight(id));
					model.addAttribute("fight", mc.getBotFight(id));
					return "fight";						
				}
				
				model.addAttribute("events", mc.getArenaEvents(userId));
			}
			model.addAttribute("menuActiveItem", "arena");
			return "arena";
		} else
		{
			model.addAttribute("failureMessage",
					"Войдите в систему или зарегистрируйтесь, чтобы отправлять ботов.");
			model.addAttribute("menuActiveItem", "registration");
			return "registration";
		}
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String doDefaultAction(ModelMap model, HttpServletRequest request)
	{
		return doAction(null, model, request);
	}

}