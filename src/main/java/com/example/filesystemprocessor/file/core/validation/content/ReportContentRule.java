package com.example.filesystemprocessor.file.core.validation.content;

import com.example.filesystemprocessor.file.core.validation.FileValidationContext;
import com.example.filesystemprocessor.file.core.i18n.MessageKey;
import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.file.core.model.FileType;

public class ReportContentRule implements ContentRule {

    private static final int MINIMUM_ROWS = 3;

    @Override
    public FileType getSupportedType() {
        return FileType.REPORT;
    }

    @Override
    public void validate(FileValidationContext context) {
        File file = context.getFile();
        int rowCount = file.getContent().split("\\R").length;

        if (rowCount < MINIMUM_ROWS) {
            context.addError(MessageKey.FILE_CONTENT_INVALID);
        }
    }
}