package com.example.filesystemprocessor.file.core.validation;

import com.example.filesystemprocessor.file.core.i18n.MessageKey;
import com.example.filesystemprocessor.file.core.model.File;

public class FileNameValidator extends AbstractFileValidator {

    @Override
    protected void validateCurrent(FileValidationContext context) {
        File file = context.getFile();

        if (file.getName() == null || file.getName().isBlank()) {
            context.addError(MessageKey.FILE_NAME_REQUIRED);
        }
    }
}