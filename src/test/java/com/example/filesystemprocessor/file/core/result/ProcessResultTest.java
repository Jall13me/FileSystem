package com.example.filesystemprocessor.file.core.result;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProcessResultTest {

    @Test
    void shouldTrackSuccessFailureAndErrors() {
        ProcessResult result = new ProcessResult();

        result.addSuccess();
        result.addFailure("contract.txt", "file.extension.invalid");

        assertEquals(2, result.getTotalProcessed());
        assertEquals(1, result.getSuccessCount());
        assertEquals(1, result.getFailureCount());
        assertTrue(result.hasFailures());
        assertFalse(result.isSuccessful());
        assertEquals("contract.txt", result.getErrors().get(0).getElementName());
        assertEquals("file.extension.invalid", result.getErrors().get(0).getMessageKey());
    }

    @Test
    void shouldMergeOtherResultAndIgnoreNullMerge() {
        ProcessResult first = new ProcessResult();
        first.addSuccess();
        ProcessResult second = new ProcessResult();
        second.addFailure("report.csv", "file.content.invalid");

        first.merge(null);
        first.merge(second);

        assertEquals(2, first.getTotalProcessed());
        assertEquals(1, first.getSuccessCount());
        assertEquals(1, first.getFailureCount());
        assertEquals(1, first.getErrors().size());
    }

    @Test
    void errorsShouldBeUnmodifiable() {
        ProcessResult result = new ProcessResult();

        assertThrows(UnsupportedOperationException.class, () ->
                result.getErrors().add(new ProcessError("x", "y")));
    }
}
