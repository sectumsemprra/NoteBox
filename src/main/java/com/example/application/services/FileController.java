package com.example.application.services;

import com.example.application.entity.FileEntity;
import com.example.application.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.example.application.services.FileControllerFromDash.getResponseEntity;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping
    public ResponseEntity<byte[]> getFile(@RequestParam String title) throws IOException {
        return getResponseEntity(title, fileService);
    }
}
