package com.example.filesystemprocessor.adapters.input.web;

import java.util.List;

public record FolderUploadRequest(
        String name,
        List<FileUploadRequest> files,
        List<FolderUploadRequest> subFolders
) {
}