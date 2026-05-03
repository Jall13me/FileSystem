package com.example.filesystemprocessor.file.core.validation;

import com.example.filesystemprocessor.file.core.i18n.MessageKey;
import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.file.core.model.FileType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileExtensionValidatorTest {

    @Test
    void shouldAcceptAllowedExtensions() {
        assertNoErrors("invoice.xml", FileType.INVOICE);
        assertNoErrors("invoice.json", FileType.INVOICE);
        assertNoErrors("contract.pdf", FileType.CONTRACT);
        assertNoErrors("report.csv", FileType.REPORT);
        assertNoErrors("report.xlsx", FileType.REPORT);
    }

    @Test
    void shouldRejectInvalidExtension() {
        FileValidationContext context = new FileValidationContext(
                new File("contract.txt", FileType.CONTRACT, 100L, "clientName=Acme signed=true")
        );

        new FileExtensionValidator().validate(context);

        assertTrue(context.getErrorKeys().contains(MessageKey.FILE_EXTENSION_INVALID));
    }

    private void assertNoErrors(String name, FileType type) {
        FileValidationContext context = new FileValidationContext(new File(name, type, 100L, "content"));
        new FileExtensionValidator().validate(context);
        assertFalse(context.hasErrors());
    }
}
