package com.example.filesystemprocessor.file.core.validation;

import com.example.filesystemprocessor.file.core.i18n.MessageKey;
import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.file.core.model.FileType;

import java.util.List;
import java.util.Map;

public class FileExtensionValidator extends AbstractFileValidator {

    private static final Map<FileType, List<String>> ALLOWED_EXTENSIONS = Map.of(
            FileType.INVOICE, List.of(".xml", ".json"),
            FileType.CONTRACT, List.of(".pdf"),
            FileType.REPORT, List.of(".csv", ".xlsx")
    );

    @Override
    protected void validateCurrent(FileValidationContext context) {
        File file = context.getFile();

        List<String> allowedExtensions = ALLOWED_EXTENSIONS.get(file.getFileType());

        if (allowedExtensions == null || allowedExtensions.stream().noneMatch(file.getName()::endsWith)) {
            context.addError(MessageKey.FILE_EXTENSION_INVALID);
        }
    }
}