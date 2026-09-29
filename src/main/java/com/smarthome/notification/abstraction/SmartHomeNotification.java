package com.smarthome.notification.abstraction;

import com.smarthome.notification.channel.NotificationChannel;
import com.smarthome.notification.exception.ChannelDeliveryException;

public abstract class SmartHomeNotification {

    protected NotificationChannel channel;

    public SmartHomeNotification(NotificationChannel channel) {
        this.channel = channel;
    }

    public void setChannel(NotificationChannel channel) {
        this.channel = channel;
    }

    public abstract void notifyUser(String recipient, String detailMessage) throws ChannelDeliveryException;
}