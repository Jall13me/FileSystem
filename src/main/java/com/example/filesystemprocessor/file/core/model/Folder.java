package com.example.filesystemprocessor.file.core.model;

import com.example.filesystemprocessor.file.core.exception.DomainException;
import com.example.filesystemprocessor.file.core.i18n.MessageKey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Folder implements FileSystemElement {

    private final String name;
    private final List<FileSystemElement> children = new ArrayList<>();

    public Folder(String name) {
        if (name == null || name.isBlank()) {
            throw new DomainException(MessageKey.FOLDER_NAME_REQUIRED);
        }

        this.name = name;
    }

    public void add(FileSystemElement element) {
        if (element == null) {
            throw new DomainException(MessageKey.ELEMENT_REQUIRED);
        }

        children.add(element);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getSize() {
        return children.stream()
                .mapToLong(FileSystemElement::getSize)
                .sum();
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    @Override
    public List<FileSystemElement> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public String toString() {
        return "Folder{" +
                "name='" + name + '\'' +
                ", children=" + children.size() +
                '}';
    }
}