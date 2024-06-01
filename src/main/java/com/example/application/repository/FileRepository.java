package com.example.application.repository;

import com.example.application.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.File;
import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Integer> {
    List<FileEntity> findByUsername(String username);
    FileEntity findByfileTitle(String title);
}