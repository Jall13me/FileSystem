package com.example.filesystemprocessor.adapters.output.notification;

public class EmailNotifier extends AbstractNotifier {

    @Override
    public NotifierType getType() {
        return NotifierType.EMAIL;
    }

    @Override
    protected void doNotify(String formattedMessage) {
        System.out.println("[EMAIL] " + formattedMessage);
    }
}