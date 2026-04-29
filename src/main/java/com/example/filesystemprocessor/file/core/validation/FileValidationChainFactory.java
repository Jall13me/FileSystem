package com.example.filesystemprocessor.file.core.validation;

import com.example.filesystemprocessor.file.core.validation.content.ContractContentRule;
import com.example.filesystemprocessor.file.core.validation.content.ContentRule;
import com.example.filesystemprocessor.file.core.validation.content.InvoiceContentRule;
import com.example.filesystemprocessor.file.core.validation.content.ReportContentRule;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FileValidationChainFactory {

    public FileValidator createDefaultChain() {
        FileValidator nameValidator = new FileNameValidator();
        FileValidator extensionValidator = new FileExtensionValidator();

        List<ContentRule> contentRules = List.of(
                new InvoiceContentRule(),
                new ContractContentRule(),
                new ReportContentRule()
        );

        FileValidator contentValidator = new FileContentValidator(contentRules);

        nameValidator.setNext(extensionValidator);
        extensionValidator.setNext(contentValidator);

        return nameValidator;
    }
}