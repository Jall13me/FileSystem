package com.example.filesystemprocessor.file.core.processing;

import com.example.filesystemprocessor.adapters.output.notification.NotificationService;
import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.file.core.model.FileType;
import com.example.filesystemprocessor.file.core.validation.FileValidationChainFactory;
import com.example.filesystemprocessor.ports.output.FileRepository;
import org.springframework.stereotype.Component;

@Component
public class ReportProcessingStrategy extends AbstractFileProcessingStrategy {

    public ReportProcessingStrategy(
            FileRepository fileRepository,
            NotificationService notificationService,
            FileValidationChainFactory validationChainFactory
    ) {
        super(fileRepository, notificationService, validationChainFactory);
    }

    @Override
    public FileType getSupportedType() {
        return FileType.REPORT;
    }

    @Override
    protected File transform(File file) {
        int rowCount = file.getContent().split("\\R").length;

        String summaryContent = "[REPORT_SUMMARY] rows=" + rowCount + " | " + file.getContent();

        return new File(
                file.getName(),
                file.getFileType(),
                file.getSize(),
                summaryContent
        );
    }
}