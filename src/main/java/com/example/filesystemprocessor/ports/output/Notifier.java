package com.example.filesystemprocessor.ports.output;

import com.example.filesystemprocessor.adapters.output.notification.NotifierType;
import com.example.filesystemprocessor.file.core.model.File;

public interface Notifier {

    NotifierType getType();

    void notify(File file, String messageKey);

}
