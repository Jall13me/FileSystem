package com.example.filesystemprocessor.file.core.i18n;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

class MessageKeyTest {

    @Test
    void shouldExposeExpectedKeysAndHavePrivateConstructor() throws Exception {
        assertEquals("file.name.required", MessageKey.FILE_NAME_REQUIRED);
        assertEquals("file.type.required", MessageKey.FILE_TYPE_REQUIRED);
        assertEquals("element.required", MessageKey.ELEMENT_REQUIRED);
        assertEquals("folder.name.required", MessageKey.FOLDER_NAME_REQUIRED);
        assertEquals("file.processed.successfully", MessageKey.FILE_PROCESSED_SUCCESSFULLY);
        assertEquals("file.extension.unsupported", MessageKey.FILE_EXTENSION_UNSUPPORTED);

        Constructor<MessageKey> constructor = MessageKey.class.getDeclaredConstructor();
        assertFalse(constructor.canAccess(null));
        constructor.setAccessible(true);
        assertNotNull(constructor.newInstance());
    }
}
