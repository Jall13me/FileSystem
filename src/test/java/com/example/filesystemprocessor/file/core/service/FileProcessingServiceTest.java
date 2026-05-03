package com.example.filesystemprocessor.file.core.service;

import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.file.core.model.FileType;
import com.example.filesystemprocessor.file.core.model.Folder;
import com.example.filesystemprocessor.file.core.result.ProcessResult;
import com.example.filesystemprocessor.testsupport.ProcessingTestSupport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileProcessingServiceTest {

    @Test
    void shouldDelegateFileAndFolderProcessing() {
        ProcessingTestSupport.TestContext context = ProcessingTestSupport.createFullContext();
        FileProcessingService service = context.processingService();

        ProcessResult fileResult = service.processFile(
                new File("invoice.xml", FileType.INVOICE, 100L, "customerId=1 amount=100")
        );
        Folder folder = new Folder("root");
        folder.add(new File("report.csv", FileType.REPORT, 100L, "h1,h2\n1,2\n3,4"));
        ProcessResult folderResult = service.processFolder(folder);

        assertEquals(1, fileResult.getSuccessCount());
        assertEquals(1, folderResult.getSuccessCount());
    }
}
