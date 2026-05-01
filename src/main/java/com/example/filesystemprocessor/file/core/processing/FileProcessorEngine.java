package com.example.filesystemprocessor.file.core.processing;

import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.file.core.model.FileSystemElement;
import com.example.filesystemprocessor.file.core.result.ProcessResult;
import org.springframework.stereotype.Service;

@Service
public class FileProcessorEngine {

    private final StrategyFactory strategyFactory;

    public FileProcessorEngine(StrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    public ProcessResult process(FileSystemElement element) {
        if (element.isDirectory()) {
            return processDirectory(element);
        }

        return processFile((File) element);
    }

    private ProcessResult processDirectory(FileSystemElement directory) {
        ProcessResult result = new ProcessResult();

        for (FileSystemElement child : directory.getChildren()) {
            ProcessResult childResult = process(child);
            result.merge(childResult);
        }

        return result;
    }

    private ProcessResult processFile(File file) {
        FileProcessingStrategy strategy = strategyFactory.getStrategy(file.getFileType());

        if (strategy == null) {
            return strategyFactory.unsupportedTypeResult(file.getName());
        }

        return strategy.process(file);
    }
}