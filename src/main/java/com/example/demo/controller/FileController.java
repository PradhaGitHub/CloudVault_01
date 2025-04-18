package com.example.demo.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/api/files")
public class FileController {

    private final String uploadDir = "C:/CloudVault/uploads/";

    // ✅ 1. Hello test
    @GetMapping("/hello")
    public String sayHello() {
        return "Hello from CloudVault!";
    }

    // ✅ 2. File Upload
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("No file selected", HttpStatus.BAD_REQUEST);
        }

        try {
            Files.createDirectories(Paths.get(uploadDir));

            String fileName = file.getOriginalFilename();
            Path targetLocation = Paths.get(uploadDir + fileName);

            file.transferTo(targetLocation);

            return new ResponseEntity<>("File uploaded successfully: " + fileName, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("File upload failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ✅ 3. File Download
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String filename) throws IOException {
        Path filePath = Paths.get(uploadDir + filename);

        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(filePath.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .body(resource);
    }

    // ✅ 4. List Files
    @GetMapping("/list")
    public ResponseEntity<String[]> listFiles() {
        File folder = new File(uploadDir);
        String[] fileList = folder.list();

        if (fileList == null) {
            return new ResponseEntity<>(new String[]{}, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(fileList, HttpStatus.OK);
    }

    // Render Download page
    @GetMapping("/download-page")
    public String showDownloadPage() {
        return "download";  // This will render download.html
    }
}
