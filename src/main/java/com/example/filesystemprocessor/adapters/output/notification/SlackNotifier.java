package com.example.filesystemprocessor.adapters.output.notification;

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