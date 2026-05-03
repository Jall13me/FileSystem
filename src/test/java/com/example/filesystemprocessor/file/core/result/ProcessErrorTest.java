package com.example.filesystemprocessor.file.core.result;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProcessErrorTest {

    @Test
    void shouldExposeValues() {
        ProcessError error = new ProcessError("invoice.xml", "file.content.invalid");

        assertEquals("invoice.xml", error.getElementName());
        assertEquals("file.content.invalid", error.getMessageKey());
    }
}
