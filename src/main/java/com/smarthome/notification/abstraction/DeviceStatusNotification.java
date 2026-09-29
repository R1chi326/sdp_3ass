package com.smarthome.notification.abstraction;

import com.smarthome.notification.channel.NotificationChannel;
import com.smarthome.notification.exception.ChannelDeliveryException;

public class DeviceStatusNotification extends SmartHomeNotification {

    private final String deviceName;

    public DeviceStatusNotification(NotificationChannel channel, String deviceName) {
        super(channel);
        this.deviceName = deviceName;
    }

    @Override
    public void notifyUser(String recipient, String detailMessage) throws ChannelDeliveryException {
        String header = "[INFO] " + deviceName;
        String body = "Status Update: " + detailMessage;
        channel.send(recipient, header, body);
    }
}