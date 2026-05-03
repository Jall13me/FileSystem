package com.example.filesystemprocessor.file.core.validation.content;

import com.example.filesystemprocessor.file.core.i18n.MessageKey;
import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.file.core.model.FileType;
import com.example.filesystemprocessor.file.core.validation.FileValidationContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReportContentRuleTest {

    @Test
    void shouldValidateMinimumRows() {
        ReportContentRule rule = new ReportContentRule();
        FileValidationContext valid = new FileValidationContext(
                new File("report.csv", FileType.REPORT, 100L, "h1,h2\n1,2\n3,4")
        );
        FileValidationContext invalid = new FileValidationContext(
                new File("report.csv", FileType.REPORT, 100L, "h1,h2\n1,2")
        );

        rule.validate(valid);
        rule.validate(invalid);

        assertEquals(FileType.REPORT, rule.getSupportedType());
        assertFalse(valid.hasErrors());
        assertTrue(invalid.getErrorKeys().contains(MessageKey.FILE_CONTENT_INVALID));
    }
}
