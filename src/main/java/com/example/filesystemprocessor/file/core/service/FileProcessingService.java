package com.example.filesystemprocessor.file.core.service;

import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.file.core.model.Folder;
import com.example.filesystemprocessor.file.core.processing.FileProcessorEngine;
import com.example.filesystemprocessor.file.core.result.ProcessResult;
import org.springframework.stereotype.Service;

@Service
public class FileProcessingService {

    private final FileProcessorEngine processorEngine;

    public FileProcessingService(FileProcessorEngine processorEngine) {
        this.processorEngine = processorEngine;
    }

    public ProcessResult processFile(File file) {
        return processorEngine.process(file);
    }

    public ProcessResult processFolder(Folder folder) {
        return processorEngine.process(folder);
    }
}