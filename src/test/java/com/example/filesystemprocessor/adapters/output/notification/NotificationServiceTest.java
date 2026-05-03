package com.example.filesystemprocessor.adapters.output.notification;

import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.file.core.model.FileType;
import com.example.filesystemprocessor.ports.output.Notifier;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NotificationServiceTest {

    @Test
    void shouldNotifyConfiguredChannels() {
        CapturingNotifier email = new CapturingNotifier(NotifierType.EMAIL);
        CapturingNotifier slack = new CapturingNotifier(NotifierType.SLACK);
        CapturingNotifier sms = new CapturingNotifier(NotifierType.SMS);
        NotificationService service = new NotificationService(List.of(email, slack, sms), new NotificationConfig());
        File contract = new File("contract.pdf", FileType.CONTRACT, 100L, "clientName=Acme signed=true");

        service.notifyProcessed(contract, "processed");

        assertEquals(1, email.messages.size());
        assertEquals(1, slack.messages.size());
        assertTrue(sms.messages.isEmpty());
    }

    @Test
    void shouldIgnoreMissingNotifierForConfiguredChannel() {
        CapturingNotifier email = new CapturingNotifier(NotifierType.EMAIL);
        NotificationService service = new NotificationService(List.of(email), new NotificationConfig());
        File report = new File("report.csv", FileType.REPORT, 100L, "a,b\n1,2\n3,4");

        assertDoesNotThrow(() -> service.notifyProcessed(report, "processed"));
        assertTrue(email.messages.isEmpty());
    }

    private static class CapturingNotifier implements Notifier {
        private final NotifierType type;
        private final List<String> messages = new ArrayList<>();

        private CapturingNotifier(NotifierType type) {
            this.type = type;
        }

        @Override
        public NotifierType getType() {
            return type;
        }

        @Override
        public void notify(File file, String messageKey) {
            messages.add(file.getName() + ":" + messageKey);
        }
    }
}
