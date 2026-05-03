package com.example.filesystemprocessor.adapters.output.notification;

import com.example.filesystemprocessor.file.core.exception.DomainException;
import com.example.filesystemprocessor.file.core.i18n.MessageKey;
import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.file.core.model.FileType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbstractNotifierTest {

    @Test
    void shouldFormatMessageAndDelegateNotification() {
        CapturingNotifier notifier = new CapturingNotifier();
        File file = new File("invoice.xml", FileType.INVOICE, 100L, "customerId=1 amount=100");

        notifier.notify(file, MessageKey.FILE_PROCESSED_SUCCESSFULLY);

        assertTrue(notifier.lastMessage.contains("invoice.xml"));
        assertTrue(notifier.lastMessage.contains("INVOICE"));
        assertTrue(notifier.lastMessage.contains(MessageKey.FILE_PROCESSED_SUCCESSFULLY));
    }

    @Test
    void shouldRejectNullFile() {
        CapturingNotifier notifier = new CapturingNotifier();

        DomainException exception = assertThrows(DomainException.class,
                () -> notifier.notify(null, "message.key"));

        assertEquals(MessageKey.FILE_NAME_REQUIRED, exception.getMessageKey());
    }

    private static class CapturingNotifier extends AbstractNotifier {
        private String lastMessage;

        @Override
        public NotifierType getType() {
            return NotifierType.EMAIL;
        }

        @Override
        protected void doNotify(String formattedMessage) {
            this.lastMessage = formattedMessage;
        }
    }
}
