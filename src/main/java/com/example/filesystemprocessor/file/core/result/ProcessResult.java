package com.example.filesystemprocessor.file.core.result;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class ProcessResult {

    private int totalProcessed;
    private int successCount;
    private int failureCount;
    private final List<ProcessError> errors = new ArrayList<>();

    public void addSuccess() {
        totalProcessed++;
        successCount++;
    }

    public void addFailure(String elementName, String messageKey) {
        totalProcessed++;
        failureCount++;
        errors.add(new ProcessError(elementName, messageKey));
    }

    public void merge(ProcessResult other) {
        if (other == null) {
            return;
        }

        this.totalProcessed += other.totalProcessed;
        this.successCount += other.successCount;
        this.failureCount += other.failureCount;
        this.errors.addAll(other.errors);
    }

    public List<ProcessError> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    public boolean hasFailures() {
        return failureCount > 0;
    }

    public boolean isSuccessful() {
        return failureCount == 0;
    }
}