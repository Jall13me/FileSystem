package com.example.filesystemprocessor.file.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DomainExceptionTest {

    @Test
    void shouldStoreMessageKeyAsExceptionMessage() {
        DomainException exception = new DomainException("file.name.required");

        assertEquals("file.name.required", exception.getMessageKey());
        assertEquals("file.name.required", exception.getMessage());
    }
}
