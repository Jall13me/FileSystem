package com.example.filesystemprocessor.file.core.validation.content;

import com.example.filesystemprocessor.file.core.i18n.MessageKey;
import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.file.core.model.FileType;
import com.example.filesystemprocessor.file.core.validation.FileValidationContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceContentRuleTest {

    @Test
    void shouldValidateInvoiceContent() {
        InvoiceContentRule rule = new InvoiceContentRule();
        FileValidationContext valid = new FileValidationContext(
                new File("invoice.xml", FileType.INVOICE, 100L, "customerId=1 amount=100")
        );
        FileValidationContext invalid = new FileValidationContext(
                new File("invoice.xml", FileType.INVOICE, 100L, "customerId=1")
        );

        rule.validate(valid);
        rule.validate(invalid);

        assertEquals(FileType.INVOICE, rule.getSupportedType());
        assertFalse(valid.hasErrors());
        assertTrue(invalid.getErrorKeys().contains(MessageKey.FILE_CONTENT_FIELD_REQUIRED));
    }
}
