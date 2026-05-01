package com.example.filesystemprocessor.file.core.processing;

import com.example.filesystemprocessor.adapters.output.notification.NotificationService;
import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.file.core.model.FileType;
import com.example.filesystemprocessor.file.core.validation.FileValidationChainFactory;
import com.example.filesystemprocessor.ports.output.FileRepository;
import org.springframework.stereotype.Component;

@Component
public class InvoiceProcessingStrategy extends AbstractFileProcessingStrategy {

    public InvoiceProcessingStrategy(
            FileRepository fileRepository,
            NotificationService notificationService,
            FileValidationChainFactory validationChainFactory
    ) {
        super(fileRepository, notificationService, validationChainFactory);
    }

    @Override
    public FileType getSupportedType() {
        return FileType.INVOICE;
    }

    @Override
    protected File transform(File file) {
        String normalizedContent = "[INTERNAL_INVOICE_FORMAT] " + file.getContent();

        return new File(
                file.getName(),
                file.getFileType(),
                file.getSize(),
                normalizedContent
        );
    }
}