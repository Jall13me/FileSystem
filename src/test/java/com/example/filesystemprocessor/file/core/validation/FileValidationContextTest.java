package com.example.filesystemprocessor.file.core.validation;

import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.file.core.model.FileType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileValidationContextTest {

    @Test
    void shouldStoreFileAndErrors() {
        File file = new File("invoice.xml", FileType.INVOICE, 100L, "customerId=1 amount=100");
        FileValidationContext context = new FileValidationContext(file);

        assertSame(file, context.getFile());
        assertFalse(context.hasErrors());

        context.addError("file.extension.invalid");

        assertTrue(context.hasErrors());
        assertEquals("file.extension.invalid", context.getErrorKeys().get(0));
        assertThrows(UnsupportedOperationException.class, () -> context.getErrorKeys().add("x"));
    }
}
