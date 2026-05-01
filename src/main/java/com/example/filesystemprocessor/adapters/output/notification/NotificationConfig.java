package com.example.filesystemprocessor.adapters.output.notification;

import com.example.filesystemprocessor.file.core.model.FileType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class NotificationConfig {

    private final Map<FileType, List<NotifierType>> channelsByFileType = Map.of(
            FileType.INVOICE, List.of(NotifierType.EMAIL),
            FileType.CONTRACT, List.of(NotifierType.EMAIL, NotifierType.SLACK),
            FileType.REPORT, List.of(NotifierType.SLACK)
    );

    public List<NotifierType> getChannelsFor(FileType fileType) {
        return channelsByFileType.getOrDefault(fileType, List.of());
    }
}