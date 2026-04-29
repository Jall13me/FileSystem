package com.example.filesystemprocessor.file.core.validation;

public interface FileValidator {

    void validate(FileValidationContext context);

    void setNext(FileValidator nextValidator);
}