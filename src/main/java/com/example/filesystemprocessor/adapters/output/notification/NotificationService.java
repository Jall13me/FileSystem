package com.example.filesystemprocessor.adapters.output.notification;

import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.ports.output.Notifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final Map<NotifierType, Notifier> notifiersByType;
    private final NotificationConfig notificationConfig;

    public NotificationService(List<Notifier> notifiers, NotificationConfig notificationConfig) {
        this.notifiersByType = notifiers.stream()
                .collect(Collectors.toMap(Notifier::getType, Function.identity()));

        this.notificationConfig = notificationConfig;
    }

    public void notifyProcessed(File file, String messageKey) {
        List<NotifierType> channels = notificationConfig.getChannelsFor(file.getFileType());

        for (NotifierType channel : channels) {
            Notifier notifier = notifiersByType.get(channel);

            if (notifier != null) {
                notifier.notify(file, messageKey);
            }
        }
    }
}