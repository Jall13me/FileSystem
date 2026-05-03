package com.example.filesystemprocessor.ports.output;

import com.example.filesystemprocessor.adapters.output.notification.NotifierType;
import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.file.core.model.FileType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PortsContractTest {

    @Test
    void shouldUseRepositoryPortImplementation() {
        FileRepository repository = new FakeRepository();
        File file = new File("invoice.xml", FileType.INVOICE, 100L, "customerId=1 amount=100");

        repository.save(file);

        assertEquals(1, repository.findAll().size());
        assertTrue(repository.findByName("invoice.xml").isPresent());
        repository.clear();
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void shouldUseNotifierPortImplementation() {
        CapturingNotifier notifier = new CapturingNotifier();
        File file = new File("invoice.xml", FileType.INVOICE, 100L, "customerId=1 amount=100");

        notifier.notify(file, "processed");

        assertEquals(NotifierType.EMAIL, notifier.getType());
        assertEquals("invoice.xml:processed", notifier.message);
    }

    private static class FakeRepository implements FileRepository {
        private final List<File> files = new ArrayList<>();

        @Override
        public void save(File file) {
            files.add(file);
        }

        @Override
        public List<File> findAll() {
            return files;
        }

        @Override
        public Optional<File> findByName(String name) {
            return files.stream().filter(file -> file.getName().equals(name)).findFirst();
        }

        @Override
        public void clear() {
            files.clear();
        }
    }

    private static class CapturingNotifier implements Notifier {
        private String message;

        @Override
        public NotifierType getType() {
            return NotifierType.EMAIL;
        }

        @Override
        public void notify(File file, String messageKey) {
            this.message = file.getName() + ":" + messageKey;
        }
    }
}
