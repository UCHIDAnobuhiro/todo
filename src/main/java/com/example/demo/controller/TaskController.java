package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Task;
import com.example.demo.repository.TaskRepository;
import com.example.demo.service.TaskService;

@Controller
public class TaskController {
	private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

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
	public String submitTask(@ModelAttribute Task task, @RequestParam("imageFile") MultipartFile imageFile,
			Model model) {
		if (!imageFile.isEmpty()) {
			try {
				File uploadDir = new File(UPLOAD_DIR);
				if (!uploadDir.exists()) {
					uploadDir.mkdirs();
				}

				String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
				File destinationFile = new File(uploadDir, fileName);

				imageFile.transferTo(destinationFile);

				task.setImageAt("/uploads/" + fileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		taskService.saveTask(task);
		List<Task> tasks = taskRepository.findAll();
		model.addAttribute("tasks", tasks);
		return "todo/database";
	}
}
