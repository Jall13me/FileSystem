package com.example.filesystemprocessor.file.core.exception;

import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {

    private final String messageKey;

    public DomainException(String messageKey) {
        super(messageKey);
        this.messageKey = messageKey;
    }

}