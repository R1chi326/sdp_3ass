package com.smarthome.notification.selector;

import com.smarthome.notification.channel.NotificationChannel;

import java.util.HashMap;
import java.util.Map;

public class DynamicChannelSelector {

    private final Map<String, NotificationChannel> channels = new HashMap<>();

    public void registerChannel(NotificationChannel channel) {
        channels.put(channel.getChannelType().toUpperCase(), channel);
    }

    public NotificationChannel selectChannel(boolean isNetworkAvailable, boolean isCritical) {
        if (!isNetworkAvailable && isCritical) {
            return channels.get("SMS");
        } else if (isCritical) {
            return channels.get("PUSH");
        } else {
            return channels.getOrDefault("EMAIL", channels.get("PUSH"));
        }
    }
}