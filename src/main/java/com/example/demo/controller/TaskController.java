package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.Task;
import com.example.demo.repository.TaskRepository;
import com.example.demo.service.TaskService;

@Controller
public class TaskController {

	@Autowired
	private TaskService taskService;

	@Autowired
	private TaskRepository taskRepository;

	@GetMapping("/todo/create")
	public String createPage(Model model) {
		model.addAttribute("task", new Task());
		return "todo/create-todo";
	}

	@GetMapping("/todo/database")
	public String databasePage(Model model) {
		List<Task> tasks = taskRepository.findAll();
		model.addAttribute("tasks", tasks);
		return "todo/database";
	}

	@PostMapping("/submit")
	public String submitTask(@ModelAttribute Task task, Model model) {
		taskService.saveTask(task);
		List<Task> tasks = taskRepository.findAll();
		model.addAttribute("tasks", tasks);
		return "todo/database";
	}
}
