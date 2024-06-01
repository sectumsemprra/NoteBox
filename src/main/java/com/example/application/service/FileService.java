package com.example.application.service;

import com.example.application.entity.FileEntity;
import com.example.application.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;
    public FileEntity saveFileEntity(FileEntity fileEntity)
    {
        return fileRepository.save(fileEntity);
    }

    public List<FileEntity> saveFileEntities(List<FileEntity> fileEntities)
    {
        return fileRepository.saveAll(fileEntities);
    }

    public List<FileEntity> getFileEntities()
    {
        return fileRepository.findAll();
    }

    public FileEntity getFileEntityById(int id)
    {
        return fileRepository.findById(id).orElse(null);
    }
    public FileEntity getFileEntityByTitle(String title)
    {
        return fileRepository.findByfileTitle(title);
    }


    public List<FileEntity> getFileEntityByUsername(String username)
    {
        return fileRepository.findByUsername(username);
    }
    public List<FileEntity> getFileEntityByUserInstitute(String userInstitute)
    {
        return fileRepository.findByUserInstitute(userInstitute);
    }
    public String deleteFileEntity(int id)
    {
        fileRepository.deleteById(id);
        return "removed" + id;
    }

    public FileEntity updateFileEntity( FileEntity fileEntity)
    {
        FileEntity existing = fileRepository.findById(fileEntity.getId()).orElse(null);
        fileRepository.delete(fileEntity);
        existing.setFileTitle(fileEntity.getFileTitle());
        existing.setFileContent(fileEntity.getFileContent());
        existing.setUsername(fileEntity.getUsername());
        existing.setUploadDate(fileEntity.uploadDate);
        existing.setUserInstitute(fileEntity.userInstitute);
        existing.setId(fileEntity.getId());
        existing.inPublicWorkspace = fileEntity.inPublicWorkspace;;
        existing.inDashboard = fileEntity.inDashboard;
        existing.userId = fileEntity.getUserId();


        return fileRepository.save(existing);
    }

}