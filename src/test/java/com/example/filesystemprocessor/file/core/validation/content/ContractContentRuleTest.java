package com.example.filesystemprocessor.file.core.validation.content;

import com.example.filesystemprocessor.file.core.i18n.MessageKey;
import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.file.core.model.FileType;
import com.example.filesystemprocessor.file.core.validation.FileValidationContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContractContentRuleTest {

    @Test
    void shouldValidateContractContent() {
        ContractContentRule rule = new ContractContentRule();
        FileValidationContext valid = new FileValidationContext(
                new File("contract.pdf", FileType.CONTRACT, 100L, "clientName=Acme signed=true")
        );
        FileValidationContext invalid = new FileValidationContext(
                new File("contract.pdf", FileType.CONTRACT, 100L, "clientName=Acme signed=false")
        );

        rule.validate(valid);
        rule.validate(invalid);

        assertEquals(FileType.CONTRACT, rule.getSupportedType());
        assertFalse(valid.hasErrors());
        assertTrue(invalid.getErrorKeys().contains(MessageKey.FILE_CONTENT_FIELD_REQUIRED));
    }
}
