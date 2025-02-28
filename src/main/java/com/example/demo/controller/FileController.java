package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/uploads")
public class FileController {
	private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

	@GetMapping("/{filename:.+}")
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
		try {
			Path filePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
			File file = filePath.toFile();

			// ファイルが存在しない場合は 404 を返す
			if (!file.exists()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}

			// リソースとして読み込む
			Resource resource = new UrlResource(file.toURI());

			// Content-Type を適切に設定
			String contentType = Files.probeContentType(filePath);
			if (contentType == null) {
				contentType = "application/octet-stream"; // デフォルト
			}

			return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType(contentType))
					.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"")
					.body(resource);
		} catch (IOException e) {
			return ResponseEntity.internalServerError().build();
		}
	}
}