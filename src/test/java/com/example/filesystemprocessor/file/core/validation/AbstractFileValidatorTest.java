package com.example.filesystemprocessor.file.core.validation;

import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.file.core.model.FileType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbstractFileValidatorTest {

    @Test
    void shouldValidateCurrentAndNextValidator() {
        TestValidator first = new TestValidator("first");
        TestValidator second = new TestValidator("second");
        first.setNext(second);
        FileValidationContext context = new FileValidationContext(
                new File("invoice.xml", FileType.INVOICE, 100L, "customerId=1 amount=100")
        );

        first.validate(context);

        assertEquals(2, context.getErrorKeys().size());
        assertEquals("first", context.getErrorKeys().get(0));
        assertEquals("second", context.getErrorKeys().get(1));
    }

    private static class TestValidator extends AbstractFileValidator {
        private final String key;

        private TestValidator(String key) {
            this.key = key;
        }

        @Override
        protected void validateCurrent(FileValidationContext context) {
            context.addError(key);
        }
    }
}
