package com.example.filesystemprocessor.file.core.service;

import com.example.filesystemprocessor.adapters.output.persistance.InMemoryFileRepository;
import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.file.core.model.FileType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileStorageServiceTest {

    @Test
    void shouldListAndClearStoredFiles() {
        InMemoryFileRepository repository = new InMemoryFileRepository();
        repository.save(new File("invoice.xml", FileType.INVOICE, 100L, "customerId=1 amount=100"));
        repository.save(new File("contract.pdf", FileType.CONTRACT, 100L, "clientName=Acme signed=true"));
        FileStorageService service = new FileStorageService(repository);

        assertEquals(List.of("invoice.xml", "contract.pdf"), service.listStoredFileNames());

        service.clearStoredFiles();

        assertTrue(repository.findAll().isEmpty());
    }
}
