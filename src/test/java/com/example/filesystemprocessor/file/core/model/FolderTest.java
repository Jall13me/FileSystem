package com.example.filesystemprocessor.file.core.model;

import com.example.filesystemprocessor.file.core.exception.DomainException;
import com.example.filesystemprocessor.file.core.i18n.MessageKey;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FolderTest {

    @Test
    void shouldCreateFolderAndCalculateSizeFromChildren() {
        Folder folder = new Folder("root");
        folder.add(new File("invoice.xml", FileType.INVOICE, 100L, "customerId=1 amount=100"));
        folder.add(new File("contract.pdf", FileType.CONTRACT, 200L, "clientName=Acme signed=true"));

        assertEquals("root", folder.getName());
        assertTrue(folder.isDirectory());
        assertEquals(300L, folder.getSize());
        assertEquals(2, folder.getChildren().size());
        assertTrue(folder.toString().contains("children=2"));
    }

    @Test
    void shouldRejectInvalidFolderNameAndNullElement() {
        DomainException blankName = assertThrows(DomainException.class, () -> new Folder(" "));
        DomainException nullName = assertThrows(DomainException.class, () -> new Folder(null));
        Folder folder = new Folder("root");
        DomainException nullElement = assertThrows(DomainException.class, () -> folder.add(null));

        assertEquals(MessageKey.FOLDER_NAME_REQUIRED, blankName.getMessageKey());
        assertEquals(MessageKey.FOLDER_NAME_REQUIRED, nullName.getMessageKey());
        assertEquals(MessageKey.ELEMENT_REQUIRED, nullElement.getMessageKey());
    }

    @Test
    void childrenListShouldBeUnmodifiable() {
        Folder folder = new Folder("root");

        assertThrows(UnsupportedOperationException.class, () ->
                folder.getChildren().add(new File("invoice.xml", FileType.INVOICE, 100L, "customerId=1 amount=100")));
    }
}
