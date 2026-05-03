package com.example.filesystemprocessor.file.core.processing;

import com.example.filesystemprocessor.file.core.i18n.MessageKey;
import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.file.core.model.FileType;
import com.example.filesystemprocessor.file.core.model.Folder;
import com.example.filesystemprocessor.file.core.result.ProcessResult;
import com.example.filesystemprocessor.testsupport.ProcessingTestSupport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileProcessorEngineTest {

    @Test
    void shouldProcessSingleFile() {
        ProcessingTestSupport.TestContext context = ProcessingTestSupport.createFullContext();
        File file = new File("invoice.xml", FileType.INVOICE, 100L, "customerId=1 amount=100");

        ProcessResult result = context.engine().process(file);

        assertEquals(1, result.getTotalProcessed());
        assertEquals(1, result.getSuccessCount());
        assertFalse(result.hasFailures());
    }

    @Test
    void shouldProcessFolderRecursivelyWithMixedResults() {
        ProcessingTestSupport.TestContext context = ProcessingTestSupport.createFullContext();
        Folder root = new Folder("root");
        Folder nested = new Folder("nested");
        root.add(new File("invoice.xml", FileType.INVOICE, 100L, "customerId=1 amount=100"));
        root.add(new File("contract.txt", FileType.CONTRACT, 100L, "clientName=Acme signed=true"));
        nested.add(new File("report.csv", FileType.REPORT, 100L, "h1,h2\n1,2\n3,4"));
        root.add(nested);

        ProcessResult result = context.engine().process(root);

        assertEquals(3, result.getTotalProcessed());
        assertEquals(2, result.getSuccessCount());
        assertEquals(1, result.getFailureCount());
        assertEquals(2, context.repository().findAll().size());
    }

    @Test
    void shouldReturnUnsupportedWhenFactoryHasNoStrategyForType() {
        StrategyFactory emptyFactory = new StrategyFactory(java.util.List.of());
        FileProcessorEngine engine = new FileProcessorEngine(emptyFactory);
        File file = new File("invoice.xml", FileType.INVOICE, 100L, "customerId=1 amount=100");

        ProcessResult result = engine.process(file);

        assertEquals(1, result.getFailureCount());
        assertEquals(MessageKey.FILE_TYPE_NOT_SUPPORTED, result.getErrors().get(0).getMessageKey());
    }
}
