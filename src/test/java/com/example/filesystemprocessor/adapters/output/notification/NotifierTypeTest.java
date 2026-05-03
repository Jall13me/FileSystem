package com.example.filesystemprocessor.adapters.output.notification;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotifierTypeTest {

    @Test
    void shouldContainExpectedValues() {
        assertEquals(NotifierType.EMAIL, NotifierType.valueOf("EMAIL"));
        assertEquals(NotifierType.SLACK, NotifierType.valueOf("SLACK"));
        assertEquals(NotifierType.SMS, NotifierType.valueOf("SMS"));
        assertEquals(3, NotifierType.values().length);
    }
}
