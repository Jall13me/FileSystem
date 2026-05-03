package com.example.filesystemprocessor.adapters.input.web;

import com.example.filesystemprocessor.file.core.model.FileType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RequestRecordTest {

    @Test
    void shouldExposeFileUploadRequestValues() {
        FileUploadRequest request = new FileUploadRequest("invoice.xml", 100L, "customerId=1 amount=100", FileType.INVOICE);

        assertEquals("invoice.xml", request.name());
        assertEquals(100L, request.size());
        assertEquals("customerId=1 amount=100", request.content());
        assertEquals(FileType.INVOICE, request.fileType());
    }

    @Test
    void shouldExposeFolderUploadRequestValues() {
        FileUploadRequest file = new FileUploadRequest("invoice.xml", 100L, "customerId=1 amount=100", FileType.INVOICE);
        FolderUploadRequest child = new FolderUploadRequest("child", List.of(file), List.of());
        FolderUploadRequest folder = new FolderUploadRequest("root", List.of(file), List.of(child));

        assertEquals("root", folder.name());
        assertEquals(1, folder.files().size());
        assertEquals(1, folder.subFolders().size());
    }
}
