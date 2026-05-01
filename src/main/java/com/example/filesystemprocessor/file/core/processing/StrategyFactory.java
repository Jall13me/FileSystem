package com.example.filesystemprocessor.file.core.processing;

import com.example.filesystemprocessor.file.core.i18n.MessageKey;
import com.example.filesystemprocessor.file.core.model.FileType;
import com.example.filesystemprocessor.file.core.result.ProcessResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class StrategyFactory {

    private final Map<FileType, FileProcessingStrategy> strategiesByType;

    public StrategyFactory(List<FileProcessingStrategy> strategies) {
        this.strategiesByType = strategies.stream()
                .collect(Collectors.toMap(FileProcessingStrategy::getSupportedType, Function.identity()));
    }

    public FileProcessingStrategy getStrategy(FileType fileType) {
        return strategiesByType.get(fileType);
    }

    public ProcessResult unsupportedTypeResult(String elementName) {
        ProcessResult result = new ProcessResult();
        result.addFailure(elementName, MessageKey.FILE_TYPE_NOT_SUPPORTED);
        return result;
    }
}