package com.sectong.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AdminController {

    /**
     * @param model
     */
    @RequestMapping(value = "/admin/", method = RequestMethod.POST)
    public String adminIndex(Model model) {
        model.addAttribute("dashboard", "dashboard");
        return "admin/index";
    }

    @RequestMapping("/admin/user")
	public String adminUser(Model model){
		model.addAttribute("user", "user");
		return "admin/user";
	}
}
