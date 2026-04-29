package com.example.filesystemprocessor.file.core.model;

import com.example.filesystemprocessor.file.core.exception.DomainException;
import com.example.filesystemprocessor.file.core.i18n.MessageKey;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class File implements FileSystemElement {

    private final String name;
    @Getter
    private final FileType fileType;
    private final long size;
    @Getter
    private final String content;
    @Getter
    private final LocalDateTime uploadedAt;

    public File(String name, FileType fileType, long size, String content) {
        if (name == null || name.isBlank()) {
            throw new DomainException(MessageKey.FILE_NAME_REQUIRED);
        }

        if (fileType == null) {
            throw new DomainException(MessageKey.FILE_TYPE_REQUIRED);
        }

        this.name = name;
        this.fileType = fileType;
        this.size = size;
        this.content = content == null ? "" : content;
        this.uploadedAt = LocalDateTime.now();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public List<FileSystemElement> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return "File{" +
                "name='" + name + '\'' +
                ", fileType=" + fileType +
                ", size=" + size +
                '}';
    }
}