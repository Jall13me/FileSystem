package com.example.filesystemprocessor.adapters.input.web;

import com.example.filesystemprocessor.file.core.exception.DomainException;
import com.example.filesystemprocessor.file.core.i18n.MessageKey;
import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.file.core.model.FileType;
import com.example.filesystemprocessor.file.core.model.Folder;
import com.example.filesystemprocessor.file.core.result.ProcessResult;
import com.example.filesystemprocessor.file.core.service.FileProcessingService;
import com.example.filesystemprocessor.file.core.service.FileStorageService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FileUploadControllerTest {

    @Test
    void shouldUploadSingleRealFile() throws Exception {
        FileProcessingService processingService = mock(FileProcessingService.class);
        FileStorageService storageService = mock(FileStorageService.class);
        ProcessResult expectedResult = new ProcessResult();
        expectedResult.addSuccess();
        when(processingService.processFile(any(File.class))).thenReturn(expectedResult);
        FileUploadController controller = new FileUploadController(processingService, storageService);
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file", "invoice.xml", "text/xml", "customerId=1 amount=100".getBytes(StandardCharsets.UTF_8)
        );

        ResponseEntity<ProcessResult> response = controller.uploadFile(multipartFile, FileType.INVOICE);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getSuccessCount());
        ArgumentCaptor<File> fileCaptor = ArgumentCaptor.forClass(File.class);
        verify(processingService).processFile(fileCaptor.capture());
        assertEquals("invoice.xml", fileCaptor.getValue().getName());
        assertEquals(FileType.INVOICE, fileCaptor.getValue().getFileType());
        assertEquals("customerId=1 amount=100", fileCaptor.getValue().getContent());
        verifyNoInteractions(storageService);
    }

    @Test
    void shouldUploadMultipleRealFilesAsFolderAndInferTypes() throws Exception {
        FileProcessingService processingService = mock(FileProcessingService.class);
        FileStorageService storageService = mock(FileStorageService.class);
        ProcessResult expectedResult = new ProcessResult();
        expectedResult.addSuccess();
        expectedResult.addSuccess();
        expectedResult.addSuccess();
        expectedResult.addSuccess();
        when(processingService.processFolder(any(Folder.class))).thenReturn(expectedResult);
        FileUploadController controller = new FileUploadController(processingService, storageService);
        List<MultipartFile> files = List.of(
                new MockMultipartFile("files", "invoice.xml", "text/xml", "customerId=1 amount=100".getBytes(StandardCharsets.UTF_8)),
                new MockMultipartFile("files", "invoice.json", "application/json", "customerId=1 amount=100".getBytes(StandardCharsets.UTF_8)),
                new MockMultipartFile("files", "contract.pdf", "application/pdf", "clientName=Acme signed=true".getBytes(StandardCharsets.UTF_8)),
                new MockMultipartFile("files", "report.xlsx", "application/vnd.ms-excel", "h1,h2\n1,2\n3,4".getBytes(StandardCharsets.UTF_8))
        );

        ResponseEntity<ProcessResult> response = controller.uploadFolder(files);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(4, response.getBody().getSuccessCount());
        ArgumentCaptor<Folder> folderCaptor = ArgumentCaptor.forClass(Folder.class);
        verify(processingService).processFolder(folderCaptor.capture());
        Folder capturedFolder = folderCaptor.getValue();
        assertEquals("uploaded-folder", capturedFolder.getName());
        assertEquals(4, capturedFolder.getChildren().size());
        assertEquals(FileType.INVOICE, ((File) capturedFolder.getChildren().get(0)).getFileType());
        assertEquals(FileType.INVOICE, ((File) capturedFolder.getChildren().get(1)).getFileType());
        assertEquals(FileType.CONTRACT, ((File) capturedFolder.getChildren().get(2)).getFileType());
        assertEquals(FileType.REPORT, ((File) capturedFolder.getChildren().get(3)).getFileType());
        verifyNoInteractions(storageService);
    }

    @Test
    void shouldInferReportTypeForCsv() throws Exception {
        FileProcessingService processingService = mock(FileProcessingService.class);
        FileStorageService storageService = mock(FileStorageService.class);
        ProcessResult expectedResult = new ProcessResult();
        expectedResult.addSuccess();
        when(processingService.processFolder(any(Folder.class))).thenReturn(expectedResult);
        FileUploadController controller = new FileUploadController(processingService, storageService);
        List<MultipartFile> files = List.of(
                new MockMultipartFile("files", "report.csv", "text/csv", "h1,h2\n1,2\n3,4".getBytes(StandardCharsets.UTF_8))
        );

        controller.uploadFolder(files);

        ArgumentCaptor<Folder> folderCaptor = ArgumentCaptor.forClass(Folder.class);
        verify(processingService).processFolder(folderCaptor.capture());
        assertEquals(FileType.REPORT, ((File) folderCaptor.getValue().getChildren().get(0)).getFileType());
    }

    @Test
    void shouldRejectUnsupportedFolderFileExtensionAndMissingName() {
        FileProcessingService processingService = mock(FileProcessingService.class);
        FileStorageService storageService = mock(FileStorageService.class);
        FileUploadController controller = new FileUploadController(processingService, storageService);

        List<MultipartFile> unsupportedFiles = List.of(
                new MockMultipartFile("files", "unknown.exe", "application/octet-stream", "content".getBytes(StandardCharsets.UTF_8))
        );
        List<MultipartFile> filesWithoutName = List.of(
                new MockMultipartFile("files", null, "text/plain", "content".getBytes(StandardCharsets.UTF_8))
        );

        DomainException unsupported = assertThrows(DomainException.class, () -> controller.uploadFolder(unsupportedFiles));
        DomainException missingName = assertThrows(DomainException.class, () -> controller.uploadFolder(filesWithoutName));

        assertEquals(MessageKey.FILE_EXTENSION_UNSUPPORTED, unsupported.getMessageKey());
        assertEquals(MessageKey.FILE_NAME_REQUIRED, missingName.getMessageKey());
        verifyNoInteractions(processingService);
        verifyNoInteractions(storageService);
    }

    @Test
    void shouldListAndClearFiles() {
        FileProcessingService processingService = mock(FileProcessingService.class);
        FileStorageService storageService = mock(FileStorageService.class);
        when(storageService.listStoredFileNames()).thenReturn(List.of("invoice.xml", "contract.pdf"));
        FileUploadController controller = new FileUploadController(processingService, storageService);

        ResponseEntity<List<String>> listResponse = controller.listFiles();
        ResponseEntity<Void> clearResponse = controller.clearFiles();

        assertEquals(200, listResponse.getStatusCode().value());
        assertEquals(List.of("invoice.xml", "contract.pdf"), listResponse.getBody());
        assertEquals(204, clearResponse.getStatusCode().value());
        verify(storageService).listStoredFileNames();
        verify(storageService).clearStoredFiles();
        verifyNoInteractions(processingService);
    }
}
