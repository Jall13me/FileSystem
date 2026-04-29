package com.example.filesystemprocessor.file.core.validation;

import com.example.filesystemprocessor.file.core.validation.content.ContentRule;
import com.example.filesystemprocessor.file.core.i18n.MessageKey;
import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.file.core.model.FileType;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FileContentValidator extends AbstractFileValidator {

    private final Map<FileType, ContentRule> rulesByType;

    public FileContentValidator(List<ContentRule> contentRules) {
        this.rulesByType = contentRules.stream()
                .collect(Collectors.toMap(ContentRule::getSupportedType, Function.identity()));
    }

    @Override
    protected void validateCurrent(FileValidationContext context) {
        File file = context.getFile();

        if (file.getContent() == null || file.getContent().isBlank()) {
            context.addError(MessageKey.FILE_CONTENT_INVALID);
            return;
        }

        ContentRule contentRule = rulesByType.get(file.getFileType());

        if (contentRule == null) {
            context.addError(MessageKey.FILE_TYPE_NOT_SUPPORTED);
            return;
        }

        contentRule.validate(context);
    }
}