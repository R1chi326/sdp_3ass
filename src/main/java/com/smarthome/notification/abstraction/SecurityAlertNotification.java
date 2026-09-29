package com.smarthome.notification.abstraction;

import com.smarthome.notification.channel.NotificationChannel;
import com.smarthome.notification.exception.ChannelDeliveryException;

public class SecurityAlertNotification extends SmartHomeNotification {

    private final String location;

    public SecurityAlertNotification(NotificationChannel channel, String location) {
        super(channel);
        this.location = location;
    }

    @Override
    public void notifyUser(String recipient, String detailMessage) throws ChannelDeliveryException {
        String header = "[SECURITY ALERT] " + location.toUpperCase();
        String body = "CRITICAL: " + detailMessage;
        channel.send(recipient, header, body);
    }
}