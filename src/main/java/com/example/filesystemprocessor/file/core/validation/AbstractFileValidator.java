package com.example.filesystemprocessor.file.core.validation;

public abstract class AbstractFileValidator implements FileValidator {

    private FileValidator nextValidator;

    @Override
    public void setNext(FileValidator nextValidator) {
        this.nextValidator = nextValidator;
    }

    @Override
    public final void validate(FileValidationContext context) {
        validateCurrent(context);

        if (nextValidator != null) {
            nextValidator.validate(context);
        }
    }

    protected abstract void validateCurrent(FileValidationContext context);
}