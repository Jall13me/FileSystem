package com.example.filesystemprocessor.adapters.output.notification;

import com.example.filesystemprocessor.file.core.exception.DomainException;
import com.example.filesystemprocessor.file.core.i18n.MessageKey;
import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.ports.output.Notifier;

public abstract class AbstractNotifier implements Notifier {

    @Override
    public final void notify(File file, String messageKey) {
        if (file == null) {
            throw new DomainException(MessageKey.FILE_NAME_REQUIRED);
        }

        String formattedMessage = formatMessage(file, messageKey);
        doNotify(formattedMessage);
    }

    protected String formatMessage(File file, String messageKey) {
        return String.format(
                "file=%s | type=%s | messageKey=%s",
                file.getName(),
                file.getFileType(),
                messageKey
        );
    }

    protected abstract void doNotify(String formattedMessage);
}