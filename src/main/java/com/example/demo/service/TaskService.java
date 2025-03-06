package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Task;
import com.example.demo.repository.TaskRepository;

@Service
public class TaskService {
	@Autowired
	private TaskRepository taskRepository;
	private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

	public List<Task> getTaskWithValidImagesByUserid(Long user_id) {
		List<Task> tasks = taskRepository.findByUserId(user_id);
		for (Task task : tasks) {
			if (task.getImageAt() != null && !task.getImageAt().isEmpty()) {
				String fileName = task.getImageAt().replace("/uploads/", "");
				File file = new File(UPLOAD_DIR, fileName);
				if (!file.exists()) {
					System.out.println("File not found! Using default image.");
					task.setImageAt("https://dummyimage.com/720x400");
				}
			} else {
				System.out.println("No image path found in database. Using default image.");
				task.setImageAt("https://dummyimage.com/720x400");
			}
		}
		return tasks;
	}

	public void saveTaskWithImage(Task task, MultipartFile imageFile) {
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
				throw new RuntimeException("ファイルのアップロードに失敗しました", e);
			}
		}
		taskRepository.save(task);
	}

	public List<Task> getAllTasks() {
		return taskRepository.findByIsDeletedFalse();
	}

	public boolean softDeleteTask(Long id) {
		Optional<Task> taskOptional = taskRepository.findById(id);
		if (taskOptional.isPresent()) {
			Task task = taskOptional.get();
			task.setDeleted(true); // isDeleted を true にする
			taskRepository.save(task);
			return true;
		}
		return false;
	}

	public boolean deletedTaskRestore(Long id) {
		Optional<Task> taskOptional = taskRepository.findById(id);
		if (taskOptional.isPresent()) {
			Task task = taskOptional.get();
			task.setDeleted(false); // isDeleted を false にする
			taskRepository.save(task);
			return true;
		}
		return false;
	}

	public Task findById(Long id) {
		Optional<Task> task = taskRepository.findById(id);
		return task.orElse(null); // タスクが存在しない場合は null を返す
	}

	public void updateTask(Task task) {
		Task existingTask = taskRepository.findById(task.getId()).orElse(null);
		if (existingTask != null) {
			existingTask.setTitle(task.getTitle());
			existingTask.setContents(task.getContents());
			existingTask.setDeadline(task.getDeadline());
			existingTask.setCategory(task.getCategory());
			if (task.getImageFile() != null && !task.getImageFile().isEmpty()) {
				saveTaskWithImage(existingTask, task.getImageFile());
			}
			taskRepository.save(existingTask);
		}
	}
}
