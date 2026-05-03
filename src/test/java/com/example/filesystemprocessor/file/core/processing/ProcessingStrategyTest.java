package com.example.filesystemprocessor.file.core.processing;

import com.example.filesystemprocessor.file.core.i18n.MessageKey;
import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.file.core.model.FileType;
import com.example.filesystemprocessor.file.core.result.ProcessResult;
import com.example.filesystemprocessor.testsupport.ProcessingTestSupport;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProcessingStrategyTest {

    @Test
    void shouldProcessValidInvoiceAndTransformContent() {
        ProcessingTestSupport.TestContext context = ProcessingTestSupport.createFullContext();
        InvoiceProcessingStrategy strategy = (InvoiceProcessingStrategy) context.strategyFactory().getStrategy(FileType.INVOICE);
        File file = new File("invoice.xml", FileType.INVOICE, 100L, "customerId=1 amount=100");

        ProcessResult result = strategy.process(file);

        assertEquals(FileType.INVOICE, strategy.getSupportedType());
        assertEquals(1, result.getSuccessCount());
        File stored = context.repository().findAll().get(0);
        assertTrue(stored.getContent().startsWith("[INTERNAL_INVOICE_FORMAT]"));
    }

    @Test
    void shouldProcessValidContractWithoutTransformation() {
        ProcessingTestSupport.TestContext context = ProcessingTestSupport.createFullContext();
        ContractProcessingStrategy strategy = (ContractProcessingStrategy) context.strategyFactory().getStrategy(FileType.CONTRACT);
        File file = new File("contract.pdf", FileType.CONTRACT, 100L, "clientName=Acme signed=true");

        ProcessResult result = strategy.process(file);

        assertEquals(FileType.CONTRACT, strategy.getSupportedType());
        assertEquals(1, result.getSuccessCount());
        assertEquals("clientName=Acme signed=true", context.repository().findAll().get(0).getContent());
    }

    @Test
    void shouldProcessValidReportAndAddSummary() {
        ProcessingTestSupport.TestContext context = ProcessingTestSupport.createFullContext();
        ReportProcessingStrategy strategy = (ReportProcessingStrategy) context.strategyFactory().getStrategy(FileType.REPORT);
        File file = new File("report.csv", FileType.REPORT, 100L, "h1,h2\n1,2\n3,4");

        ProcessResult result = strategy.process(file);

        assertEquals(FileType.REPORT, strategy.getSupportedType());
        assertEquals(1, result.getSuccessCount());
        assertTrue(context.repository().findAll().get(0).getContent().startsWith("[REPORT_SUMMARY] rows=3"));
    }

    @Test
    void shouldReturnFailureAndNotStoreWhenValidationFails() {
        ProcessingTestSupport.TestContext context = ProcessingTestSupport.createFullContext();
        FileProcessingStrategy strategy = context.strategyFactory().getStrategy(FileType.CONTRACT);
        File file = new File("contract.txt", FileType.CONTRACT, 100L, "clientName=Acme signed=true");

        ProcessResult result = strategy.process(file);

        assertEquals(1, result.getFailureCount());
        assertTrue(result.hasFailures());
        assertTrue(context.repository().findAll().isEmpty());
        assertEquals(MessageKey.FILE_EXTENSION_INVALID, result.getErrors().get(0).getMessageKey());
    }

    @Test
    void strategyFactoryShouldReturnStrategyAndUnsupportedResult() {
        ProcessingTestSupport.TestContext context = ProcessingTestSupport.createFullContext();
        StrategyFactory factory = context.strategyFactory();

        assertNotNull(factory.getStrategy(FileType.INVOICE));
        assertNull(factory.getStrategy(null));

        ProcessResult unsupported = factory.unsupportedTypeResult("unknown.bin");

        assertEquals(1, unsupported.getFailureCount());
        assertEquals(MessageKey.FILE_TYPE_NOT_SUPPORTED, unsupported.getErrors().get(0).getMessageKey());
    }

    @Test
    void duplicateStrategiesShouldThrowException() {
        ProcessingTestSupport.TestContext context = ProcessingTestSupport.createFullContext();
        FileProcessingStrategy invoice = context.strategyFactory().getStrategy(FileType.INVOICE);

        assertThrows(IllegalStateException.class, () -> new StrategyFactory(List.of(invoice, invoice)));
    }
}
