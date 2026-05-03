package com.example.filesystemprocessor;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

class FileSystemPorcessorApplicationTests {

    @Test
    void applicationClassShouldBeInstantiable() throws Exception {
        Constructor<FileSystemPorcessorApplication> constructor = FileSystemPorcessorApplication.class.getDeclaredConstructor();
        assertNotNull(constructor.newInstance());
    }
}
