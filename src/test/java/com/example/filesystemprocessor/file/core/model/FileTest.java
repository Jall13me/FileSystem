package com.example.filesystemprocessor.file.core.model;

import com.example.filesystemprocessor.file.core.exception.DomainException;
import com.example.filesystemprocessor.file.core.i18n.MessageKey;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileTest {

    @Test
    void shouldCreateFileWithValidData() {
        File file = new File("invoice.xml", FileType.INVOICE, 100L, "customerId=1 amount=100");

        assertEquals("invoice.xml", file.getName());
        assertEquals(FileType.INVOICE, file.getFileType());
        assertEquals(100L, file.getSize());
        assertEquals("customerId=1 amount=100", file.getContent());
        assertNotNull(file.getUploadedAt());
        assertFalse(file.isDirectory());
        assertTrue(file.getChildren().isEmpty());
        assertTrue(file.toString().contains("invoice.xml"));
    }

    @Test
    void shouldConvertNullContentToEmptyString() {
        File file = new File("invoice.xml", FileType.INVOICE, 100L, null);

        assertEquals("", file.getContent());
    }

    @Test
    void shouldRejectNullOrBlankName() {
        DomainException nullName = assertThrows(DomainException.class,
                () -> new File(null, FileType.INVOICE, 100L, "content"));
        DomainException blankName = assertThrows(DomainException.class,
                () -> new File(" ", FileType.INVOICE, 100L, "content"));

        assertEquals(MessageKey.FILE_NAME_REQUIRED, nullName.getMessageKey());
        assertEquals(MessageKey.FILE_NAME_REQUIRED, blankName.getMessageKey());
    }

    @Test
    void shouldRejectNullType() {
        DomainException exception = assertThrows(DomainException.class,
                () -> new File("invoice.xml", null, 100L, "content"));

        assertEquals(MessageKey.FILE_TYPE_REQUIRED, exception.getMessageKey());
    }
}
