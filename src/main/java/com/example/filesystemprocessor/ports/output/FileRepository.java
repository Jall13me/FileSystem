package com.example.filesystemprocessor.ports.output;

import com.example.filesystemprocessor.file.core.model.File;

import java.util.List;
import java.util.Optional;

public interface FileRepository {

    void save(File file);

    List<File> findAll();

    Optional<File> findByName(String name);

    void clear();
}