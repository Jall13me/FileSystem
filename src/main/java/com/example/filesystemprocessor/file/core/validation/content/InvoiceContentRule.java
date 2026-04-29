package com.example.filesystemprocessor.file.core.validation.content;

import com.example.filesystemprocessor.file.core.validation.FileValidationContext;
import com.example.filesystemprocessor.file.core.i18n.MessageKey;
import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.file.core.model.FileType;

public class InvoiceContentRule implements ContentRule {

    @Override
    public FileType getSupportedType() {
        return FileType.INVOICE;
    }

    @Override
    public void validate(FileValidationContext context) {
        File file = context.getFile();

        if (!file.getContent().contains("customerId")) {
            context.addError(MessageKey.FILE_CONTENT_FIELD_REQUIRED);
        }

        if (!file.getContent().contains("amount")) {
            context.addError(MessageKey.FILE_CONTENT_FIELD_REQUIRED);
        }
    }
}