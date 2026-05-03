package com.example.filesystemprocessor.file.core.validation;

import com.example.filesystemprocessor.file.core.i18n.MessageKey;
import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.file.core.model.FileType;
import com.example.filesystemprocessor.file.core.validation.content.InvoiceContentRule;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileContentValidatorTest {

    @Test
    void shouldRejectBlankContent() {
        FileValidationContext context = new FileValidationContext(
                new File("invoice.xml", FileType.INVOICE, 100L, " ")
        );

        new FileContentValidator(List.of(new InvoiceContentRule())).validate(context);

        assertTrue(context.getErrorKeys().contains(MessageKey.FILE_CONTENT_INVALID));
    }

    @Test
    void shouldUseRegisteredContentRule() {
        FileValidationContext context = new FileValidationContext(
                new File("invoice.xml", FileType.INVOICE, 100L, "customerId=1 amount=100")
        );

        new FileContentValidator(List.of(new InvoiceContentRule())).validate(context);

        assertFalse(context.hasErrors());
    }

    @Test
    void shouldRejectMissingContentRule() {
        FileValidationContext context = new FileValidationContext(
                new File("contract.pdf", FileType.CONTRACT, 100L, "clientName=Acme signed=true")
        );

        new FileContentValidator(List.of(new InvoiceContentRule())).validate(context);

        assertTrue(context.getErrorKeys().contains(MessageKey.FILE_TYPE_NOT_SUPPORTED));
    }
}
