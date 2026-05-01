package com.example.filesystemprocessor.file.core.processing;

import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.file.core.model.FileType;
import com.example.filesystemprocessor.file.core.result.ProcessResult;

public interface FileProcessingStrategy {

    FileType getSupportedType();

    ProcessResult process(File file);
}