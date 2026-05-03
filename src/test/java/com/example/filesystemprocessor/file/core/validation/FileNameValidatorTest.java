package com.example.filesystemprocessor.file.core.validation;

import com.example.filesystemprocessor.file.core.i18n.MessageKey;
import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.file.core.model.FileType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileNameValidatorTest {

    @Test
    void shouldAcceptValidName() {
        FileValidationContext context = new FileValidationContext(
                new File("invoice.xml", FileType.INVOICE, 100L, "customerId=1 amount=100")
        );

        new FileNameValidator().validate(context);

        assertFalse(context.hasErrors());
    }

    @Test
    void shouldRejectBlankNameUsingFakeFile() {
        FileValidationContext context = new FileValidationContext(new BlankNameFile());

        new FileNameValidator().validate(context);

        assertTrue(context.getErrorKeys().contains(MessageKey.FILE_NAME_REQUIRED));
    }

    private static class BlankNameFile extends File {
        private BlankNameFile() {
            super("valid.xml", FileType.INVOICE, 100L, "customerId=1 amount=100");
        }

        @Override
        public String getName() {
            return " ";
        }
    }
}
