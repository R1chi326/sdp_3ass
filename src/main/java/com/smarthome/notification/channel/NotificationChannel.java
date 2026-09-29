package com.smarthome.notification.channel;

import com.smarthome.notification.exception.ChannelDeliveryException;

public interface NotificationChannel {
    void send(String recipient, String header, String body) throws ChannelDeliveryException;
    String getChannelType();
}