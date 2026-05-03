package com.example.filesystemprocessor.file.core.service;

import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.ports.output.FileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileStorageService {

    private final FileRepository fileRepository;

    public FileStorageService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public List<String> listStoredFileNames() {
        return fileRepository.findAll()
                .stream()
                .map(File::getName)
                .toList();
    }

    public void clearStoredFiles() {
        fileRepository.clear();
    }
}
