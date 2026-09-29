package com.smarthome.notification.channel;

import com.smarthome.notification.exception.ChannelDeliveryException;

public class PushNotificationChannel implements NotificationChannel {
    @Override
    public void send(String recipient, String header, String body) throws ChannelDeliveryException {
        if (recipient == null || recipient.isBlank()) {
            throw new ChannelDeliveryException("Push token is invalid or missing.");
        }
        System.out.println("[PUSH] Sent to " + recipient + " | " + header + " - " + body);
    }

    @Override
    public String getChannelType() {
        return "PUSH";
    }
}