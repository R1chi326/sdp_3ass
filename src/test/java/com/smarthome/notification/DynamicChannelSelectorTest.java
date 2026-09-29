package com.smarthome.notification;

import com.smarthome.notification.channel.NotificationChannel;
import com.smarthome.notification.exception.ChannelDeliveryException;
import com.smarthome.notification.selector.DynamicChannelSelector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DynamicChannelSelectorTest {

    private static class DummySmsChannel implements NotificationChannel {
        @Override
        public void send(String recipient, String header, String body) throws ChannelDeliveryException {}

        @Override
        public String getChannelType() {
            return "SMS";
        }
    }

    @Test
    void testSelectsSmsWhenNetworkIsDownAndCritical() {
        DynamicChannelSelector selector = new DynamicChannelSelector();
        NotificationChannel smsChannel = new DummySmsChannel();
        selector.registerChannel(smsChannel);

        NotificationChannel selected = selector.selectChannel(false, true);
        assertEquals(smsChannel, selected);
    }
}