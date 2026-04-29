package com.example.filesystemprocessor.file.core.validation.content;

import com.example.filesystemprocessor.file.core.validation.FileValidationContext;
import com.example.filesystemprocessor.file.core.i18n.MessageKey;
import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.file.core.model.FileType;

public class ContractContentRule implements ContentRule{

    @Override
    public FileType getSupportedType(){
        return FileType.CONTRACT;
    }

    @Override
    public void validate(FileValidationContext context){
        File file = context.getFile();

        if (!file.getContent().contains("clientName")){
            context.addError(MessageKey.FILE_CONTENT_FIELD_REQUIRED);
        }

        if (!file.getContent().contains("signed=true")){
            context.addError(MessageKey.FILE_CONTENT_FIELD_REQUIRED);
        }

    }

}
