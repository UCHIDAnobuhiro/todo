package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.User;

@Controller
public class UserController {
	@RequestMapping("/")
	public String showLogin() {
		return "login";
	}

	@GetMapping("/")
	public String setUser(Model model) {
		model.addAttribute("user", new User());
		return "login";

	}

	@PostMapping("/show")
	public String requestMethodName(@ModelAttribute User user, Model model) {
		//データベースでuserをチェックし、あるなら/showを表示する、ないならrootに戻る
		model.addAttribute(user);
		return "/";
	}

}
