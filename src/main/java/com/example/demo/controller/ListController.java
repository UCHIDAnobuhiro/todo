package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ListController {

	@GetMapping("/todo/create")
	public String creataPage() {
		return "todo/create-todo";
	}

}
