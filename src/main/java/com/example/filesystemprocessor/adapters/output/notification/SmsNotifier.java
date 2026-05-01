package com.example.filesystemprocessor.adapters.output.notification;

import org.springframework.stereotype.Component;

@Component
public class SmsNotifier extends AbstractNotifier {

    @Override
    public NotifierType getType() {
        return NotifierType.SMS;
    }

    @Override
    protected void doNotify(String formattedMessage) {
        System.out.println("[SMS] " + formattedMessage);
    }
}