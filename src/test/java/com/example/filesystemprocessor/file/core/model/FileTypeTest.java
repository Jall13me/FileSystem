package com.example.filesystemprocessor.file.core.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileTypeTest {

    @Test
    void shouldContainExpectedValues() {
        assertEquals(FileType.INVOICE, FileType.valueOf("INVOICE"));
        assertEquals(FileType.CONTRACT, FileType.valueOf("CONTRACT"));
        assertEquals(FileType.REPORT, FileType.valueOf("REPORT"));
        assertEquals(3, FileType.values().length);
    }
}
