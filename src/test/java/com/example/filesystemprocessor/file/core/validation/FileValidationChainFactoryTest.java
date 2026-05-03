package com.example.filesystemprocessor.file.core.validation;

import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.file.core.model.FileType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileValidationChainFactoryTest {

    @Test
    void shouldCreateChainThatValidatesFile() {
        FileValidator chain = new FileValidationChainFactory().createDefaultChain();
        FileValidationContext valid = new FileValidationContext(
                new File("invoice.xml", FileType.INVOICE, 100L, "customerId=1 amount=100")
        );
        FileValidationContext invalid = new FileValidationContext(
                new File("contract.txt", FileType.CONTRACT, 100L, "clientName=Acme signed=true")
        );

        chain.validate(valid);
        chain.validate(invalid);

        assertFalse(valid.hasErrors());
        assertTrue(invalid.hasErrors());
    }
}
