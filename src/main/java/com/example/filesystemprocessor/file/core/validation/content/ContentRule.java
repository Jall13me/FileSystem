package com.example.filesystemprocessor.file.core.validation.content;

import com.example.filesystemprocessor.file.core.validation.FileValidationContext;
import com.example.filesystemprocessor.file.core.model.FileType;

public interface ContentRule {

    FileType getSupportedType();

    void validate(FileValidationContext context);

}
