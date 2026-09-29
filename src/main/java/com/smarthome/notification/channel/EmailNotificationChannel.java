package com.smarthome.notification.channel;

import com.smarthome.notification.exception.ChannelDeliveryException;

public class EmailNotificationChannel implements NotificationChannel {
    @Override
    public void send(String recipient, String header, String body) throws ChannelDeliveryException {
        if (recipient == null || !recipient.contains("@")) {
            throw new ChannelDeliveryException("Invalid email address: " + recipient);
        }
        System.out.println("[EMAIL] Sent to " + recipient + " | " + header + " - " + body);
    }

    @Override
    public String getChannelType() {
        return "EMAIL";
    }
}