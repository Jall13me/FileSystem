package com.example.filesystemprocessor.file.core.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProcessError {
    private final String elementName;
    private final String messageKey;
}
