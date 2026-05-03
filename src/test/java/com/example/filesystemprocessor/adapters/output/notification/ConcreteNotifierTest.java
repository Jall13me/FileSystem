package com.example.filesystemprocessor.adapters.output.notification;

import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.file.core.model.FileType;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ConcreteNotifierTest {

    @Test
    void shouldReturnTypesAndPrintMessages() {
        assertEquals(NotifierType.EMAIL, new EmailNotifier().getType());
        assertEquals(NotifierType.SLACK, new SlackNotifier().getType());
        assertEquals(NotifierType.SMS, new SmsNotifier().getType());

        File file = new File("invoice.xml", FileType.INVOICE, 100L, "customerId=1 amount=100");
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(output));
        try {
            new EmailNotifier().notify(file, "processed");
            new SlackNotifier().notify(file, "processed");
            new SmsNotifier().notify(file, "processed");
        } finally {
            System.setOut(original);
        }

        String printed = output.toString();
        assertTrue(printed.contains("[EMAIL]"));
        assertTrue(printed.contains("[SLACK]"));
        assertTrue(printed.contains("[SMS]"));
    }
}
