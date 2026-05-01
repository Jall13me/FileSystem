package com.example.filesystemprocessor.adapters.output.notification;

import org.springframework.stereotype.Component;

@Component
public class SlackNotifier extends AbstractNotifier {

    @Override
    public NotifierType getType() {
        return NotifierType.SLACK;
    }

    @Override
    protected void doNotify(String formattedMessage) {
        System.out.println("[SLACK] " + formattedMessage);
    }
}