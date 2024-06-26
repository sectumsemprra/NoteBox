package com.example.application.services;

import com.example.application.entity.FileEntity;
import com.example.application.service.FileService;
import jakarta.validation.constraints.NotNull;
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

@RestController
@RequestMapping("/filess")
public class FileControllerFromDash {

    @Autowired
    private FileService fileService;

    @GetMapping
    public ResponseEntity<byte[]> getFile(@RequestParam int id) throws IOException {
        return getResponseEntity(id, fileService);
    }

    @NotNull
    static ResponseEntity<byte[]> getResponseEntity(@RequestParam int id, FileService fileService) {
        FileEntity fileEntity = fileService.getFileEntityById(id);
        if (fileEntity != null) {
            if (fileEntity.getFileContent() != null && !fileEntity.textfile) {
                //byte[] pdfContent = Files.readAllBytes(Paths.get("E:/pdfs/2-2/introduction-to-probability-model-s.ross-math-cs.blog_.ir_.pdf"));

                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Disposition", "inline; filename=" + fileEntity.getFileTitle());
                headers.add("Content-Type", "application/pdf");

                System.out.println("Serving file: " + fileEntity.getFileTitle());
                System.out.println("File size: " + fileEntity.getFileContent().length + " bytes");

                //return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
                return new ResponseEntity<>(fileEntity.getFileContent(), headers, HttpStatus.OK);
            } else {
                System.err.println("File content is null or not a PDF for file title: " + fileEntity.getFileTitle());
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        System.err.println("File entity not found for file id: " + id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
