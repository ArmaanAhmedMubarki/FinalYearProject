package com.sports.athleticax.controller;

import com.sports.athleticax.dto.SquadResponseDTO;
import com.sports.athleticax.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:3000") // change if your frontend uses another port
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> chatWithCsv(
            @RequestParam("message") String message,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            if (message == null || message.isBlank()) {
                return ResponseEntity.badRequest().body("message is required");
            }

            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("CSV file is required");
            }

            String originalFilename = file.getOriginalFilename();

            if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".csv")) {
                return ResponseEntity.badRequest().body("Only CSV files are allowed");
            }

            // =====================================================
            // SAVE TO A REAL STABLE FOLDER (NOT TOMCAT TEMP)
            // =====================================================
            String uploadFolder = System.getProperty("java.io.tmpdir")
        + File.separator
        + "uploads";

            File uploadDir = new File(uploadFolder);
            
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            String filename = System.currentTimeMillis() + "_" + originalFilename;
            
            Path savedPath = Paths.get(uploadFolder, filename);
            
            Files.copy(
                    file.getInputStream(),
                    savedPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            System.out.println("Uploaded CSV saved at: " + savedPath.toAbsolutePath());

            // =====================================================
            // PASS SAVED CSV PATH TO CHAT SERVICE
            // =====================================================
            SquadResponseDTO response = chatService.ask(
                    message,
                    savedPath.toAbsolutePath().toString()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Chat processing failed: " + e.getMessage());
        }
    }
}
