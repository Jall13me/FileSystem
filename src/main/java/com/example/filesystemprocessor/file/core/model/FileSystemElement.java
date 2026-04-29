package com.example.filesystemprocessor.file.core.model;

import java.util.List;

public interface FileSystemElement {

    String getName();

    long getSize();

    boolean isDirectory();

    List<FileSystemElement> getChildren();

}
