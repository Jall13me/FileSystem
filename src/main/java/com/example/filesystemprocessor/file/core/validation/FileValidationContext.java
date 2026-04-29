package com.example.filesystemprocessor.file.core.validation;

import com.example.filesystemprocessor.file.core.model.File;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class FileValidationContext {

    private final File file;
    private final List<String> errorKeys = new ArrayList<>();

    public FileValidationContext(File file) {
        this.file = file;
    }

    public void addError(String messageKey) {
        errorKeys.add(messageKey);
    }

    public boolean hasErrors() {
        return !errorKeys.isEmpty();
    }

    public List<String> getErrorKeys() {
        return Collections.unmodifiableList(errorKeys);
    }
}