package com.xupypr.aicontest.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xupypr.aicontest.database.MongoConnector;
 
@Controller
@RequestMapping("/rating")
public class RatingController {
 
	@RequestMapping(value = "/{action}/{id}", method = RequestMethod.GET)
	public String doAction(@PathVariable String action, ModelMap model) {
		MongoConnector mc = new MongoConnector();
		model.addAttribute("bots", mc.getRating()); 
		model.addAttribute("menuActiveItem", "rating");		
		return "rating";
 	}
 
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String doDefaultAction(ModelMap model) {
		return doAction(null, model);
 	}
 
}