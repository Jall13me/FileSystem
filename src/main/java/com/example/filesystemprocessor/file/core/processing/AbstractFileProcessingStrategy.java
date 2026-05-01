package com.example.filesystemprocessor.file.core.processing;

import com.example.filesystemprocessor.adapters.output.notification.NotificationService;
import com.example.filesystemprocessor.file.core.i18n.MessageKey;
import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.file.core.result.ProcessResult;
import com.example.filesystemprocessor.file.core.validation.FileValidationChainFactory;
import com.example.filesystemprocessor.file.core.validation.FileValidationContext;
import com.example.filesystemprocessor.file.core.validation.FileValidator;
import com.example.filesystemprocessor.ports.output.FileRepository;

public abstract class AbstractFileProcessingStrategy implements FileProcessingStrategy {

    private final FileRepository fileRepository;
    private final NotificationService notificationService;
    private final FileValidationChainFactory validationChainFactory;

    protected AbstractFileProcessingStrategy(
            FileRepository fileRepository,
            NotificationService notificationService,
            FileValidationChainFactory validationChainFactory
    ) {
        this.fileRepository = fileRepository;
        this.notificationService = notificationService;
        this.validationChainFactory = validationChainFactory;
    }

    @Override
    public final ProcessResult process(File file) {
        ProcessResult result = new ProcessResult();

        FileValidationContext validationContext = new FileValidationContext(file);
        FileValidator validator = validationChainFactory.createDefaultChain();
        validator.validate(validationContext);

        if (validationContext.hasErrors()) {
            validationContext.getErrorKeys()
                    .forEach(errorKey -> result.addFailure(file.getName(), errorKey));

            return result;
        }

        File processedFile = transform(file);

        fileRepository.save(processedFile);

        notificationService.notifyProcessed(
                processedFile,
                MessageKey.FILE_PROCESSED_SUCCESSFULLY
        );

        result.addSuccess();

        return result;
    }

    protected File transform(File file) {
        return file;
    }
}