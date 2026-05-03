package com.example.filesystemprocessor.testsupport;

import com.example.filesystemprocessor.adapters.output.notification.EmailNotifier;
import com.example.filesystemprocessor.adapters.output.notification.NotificationConfig;
import com.example.filesystemprocessor.adapters.output.notification.NotificationService;
import com.example.filesystemprocessor.adapters.output.notification.SlackNotifier;
import com.example.filesystemprocessor.adapters.output.notification.SmsNotifier;
import com.example.filesystemprocessor.adapters.output.persistance.InMemoryFileRepository;
import com.example.filesystemprocessor.file.core.processing.ContractProcessingStrategy;
import com.example.filesystemprocessor.file.core.processing.FileProcessorEngine;
import com.example.filesystemprocessor.file.core.processing.FileProcessingStrategy;
import com.example.filesystemprocessor.file.core.processing.InvoiceProcessingStrategy;
import com.example.filesystemprocessor.file.core.processing.ReportProcessingStrategy;
import com.example.filesystemprocessor.file.core.processing.StrategyFactory;
import com.example.filesystemprocessor.file.core.service.FileProcessingService;
import com.example.filesystemprocessor.file.core.service.FileStorageService;
import com.example.filesystemprocessor.file.core.validation.FileValidationChainFactory;
import com.example.filesystemprocessor.ports.output.FileRepository;
import com.example.filesystemprocessor.ports.output.Notifier;

import java.util.List;

public final class ProcessingTestSupport {

    private ProcessingTestSupport() {
    }

    public static TestContext createFullContext() {
        InMemoryFileRepository repository = new InMemoryFileRepository();
        NotificationService notificationService = new NotificationService(
                List.of(new EmailNotifier(), new SlackNotifier(), new SmsNotifier()),
                new NotificationConfig()
        );
        FileValidationChainFactory validationChainFactory = new FileValidationChainFactory();

        List<FileProcessingStrategy> strategies = List.of(
                new InvoiceProcessingStrategy(repository, notificationService, validationChainFactory),
                new ContractProcessingStrategy(repository, notificationService, validationChainFactory),
                new ReportProcessingStrategy(repository, notificationService, validationChainFactory)
        );

        StrategyFactory strategyFactory = new StrategyFactory(strategies);
        FileProcessorEngine engine = new FileProcessorEngine(strategyFactory);
        return new TestContext(
                repository,
                notificationService,
                validationChainFactory,
                strategyFactory,
                engine,
                new FileProcessingService(engine),
                new FileStorageService(repository)
        );
    }

    public record TestContext(
            FileRepository repository,
            NotificationService notificationService,
            FileValidationChainFactory validationChainFactory,
            StrategyFactory strategyFactory,
            FileProcessorEngine engine,
            FileProcessingService processingService,
            FileStorageService storageService
    ) {
    }
}
