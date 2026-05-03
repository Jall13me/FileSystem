package com.example.filesystemprocessor.adapters.output.notification;

import com.example.filesystemprocessor.file.core.model.FileType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NotificationConfigTest {

    @Test
    void shouldReturnChannelsByFileType() {
        NotificationConfig config = new NotificationConfig();

        assertEquals(List.of(NotifierType.EMAIL), config.getChannelsFor(FileType.INVOICE));
        assertEquals(List.of(NotifierType.EMAIL, NotifierType.SLACK), config.getChannelsFor(FileType.CONTRACT));
        assertEquals(List.of(NotifierType.SLACK), config.getChannelsFor(FileType.REPORT));
    }
}
