package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LogController {

	//ログアウト時の処理
	@PostMapping("/logout")
	public String logoutAndCleanSession(HttpSession session) {
		//sessionを空っぽにし、ログイン画面に戻る
		session.invalidate();
		return "redirect:/login";
	}
}
