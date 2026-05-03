package com.example.filesystemprocessor.adapters.output.persistance;

import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.file.core.model.FileType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryFileRepositoryTest {

    @Test
    void shouldSaveFindAndClearFiles() {
        InMemoryFileRepository repository = new InMemoryFileRepository();
        File file = new File("invoice.xml", FileType.INVOICE, 100L, "customerId=1 amount=100");

        repository.save(file);

        assertEquals(1, repository.findAll().size());
        assertTrue(repository.findByName("invoice.xml").isPresent());
        assertTrue(repository.findByName("missing.xml").isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> repository.findAll().clear());

        repository.clear();

        assertTrue(repository.findAll().isEmpty());
    }
}
