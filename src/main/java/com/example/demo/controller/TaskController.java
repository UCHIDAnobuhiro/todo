package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Task;
import com.example.demo.repository.TaskRepository;
import com.example.demo.service.TaskService;

@Controller
public class TaskController {

	@Autowired
	private TaskService taskService;

	@Autowired
	private TaskRepository taskRepository;

	@GetMapping("todo/show")
	public String showPage(HttpSession session, Model model) {
		long userId = (long) session.getAttribute("userId");
		String userName = (String) session.getAttribute("userName");
		model.addAttribute("userName", userName);
		model.addAttribute("tasks", taskService.getTaskWithValidImagesByUserid(userId));
		return "todo/show-todo";
	}

	@GetMapping("/todo/create")
	public String createPage(Model model) {
		model.addAttribute("task", new Task());
		return "todo/create-todo";
	}

	@GetMapping("/todo/edit/{id}")
	public String editPage(@PathVariable Long id, Model model) {
		Task task = taskService.findById(id);
		if (task == null) {
			return "redirect:/todo/show";
		}
		model.addAttribute("task", task);
		return "todo/edit-todo";
	}

	@GetMapping("/todo/database")
	public String databasePage(Model model) {
		List<Task> tasks = taskRepository.findAll();
		model.addAttribute("tasks", tasks);
		return "todo/database";
	}

	@PostMapping("/todo/submit")
	public String submitTask(@ModelAttribute Task task, @RequestParam("imageFile") MultipartFile imageFile,
			Model mode, HttpSession session) {
		long userId = (long) session.getAttribute("userId");
		task.setUserId(userId);
		taskService.saveTaskWithImage(task, imageFile);
		return "redirect:/todo/show";
	}

	@PostMapping("/todo/update")
	public String updateTask(@ModelAttribute Task task) {
		taskService.updateTask(task);
		return "redirect:/todo/show";
	}

	@GetMapping
	public List<Task> getTasks() {
		return taskService.getAllTasks();
	}

	@PatchMapping("/delete/{id}")
	public ResponseEntity<Map<String, Object>> softDeleteTask(@PathVariable("id") Long id) {
		Map<String, Object> response = new HashMap<>();
		boolean result = taskService.softDeleteTask(id);
		if (result) {
			response.put("message", "タスク" + id + "論理削除をしました");
			response.put("id", id);
			return ResponseEntity.ok(response);
		} else {
			response.put("error", "タスクが見つかりません");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
	}
}
