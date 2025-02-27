package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TaskController {

	@GetMapping("/todo/create")
	public String createPage() {
		return "todo/create-todo";
	}

	@GetMapping("/todo/database")
	public String databasePage() {
		return "todo/database";
	}
}
