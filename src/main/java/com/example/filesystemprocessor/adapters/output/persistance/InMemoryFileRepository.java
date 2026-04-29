package com.example.filesystemprocessor.adapters.output.persistance;

import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.ports.output.FileRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryFileRepository implements FileRepository {

    private final List<File> files = new ArrayList<>();

    @Override
    public void save(File file) {
        files.add(file);
    }

    @Override
    public List<File> findAll() {
        return Collections.unmodifiableList(files);
    }

    @Override
    public Optional<File> findByName(String name) {
        return files.stream()
                .filter(file -> file.getName().equals(name))
                .findFirst();
    }

    @Override
    public void clear() {
        files.clear();
    }
}