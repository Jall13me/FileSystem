package com.example.filesystemprocessor.adapters.input.web;

import com.example.filesystemprocessor.file.core.model.FileType;

public record FileUploadRequest(
        String name,
        long size,
        String content,
        FileType fileType
) {
}