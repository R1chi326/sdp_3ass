package com.smarthome.notification;

import com.smarthome.notification.abstraction.SecurityAlertNotification;
import com.smarthome.notification.channel.NotificationChannel;
import com.smarthome.notification.exception.ChannelDeliveryException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BridgeNotificationTest {

    // Тестовая заглушка (Stub) для проверки передачи параметров через Bridge
    private static class TestChannel implements NotificationChannel {
        String lastRecipient;
        String lastHeader;
        String lastBody;

        @Override
        public void send(String recipient, String header, String body) throws ChannelDeliveryException {
            this.lastRecipient = recipient;
            this.lastHeader = header;
            this.lastBody = body;
        }

        @Override
        public String getChannelType() {
            return "TEST";
        }
    }

    @Test
    void testSecurityAlertDelegatesToChannel() throws ChannelDeliveryException {
        TestChannel testChannel = new TestChannel();
        SecurityAlertNotification alert = new SecurityAlertNotification(testChannel, "Garage");

        alert.notifyUser("user@example.com", "Motion detected");

        assertEquals("user@example.com", testChannel.lastRecipient);
        assertEquals("[SECURITY ALERT] GARAGE", testChannel.lastHeader);
        assertEquals("CRITICAL: Motion detected", testChannel.lastBody);
    }
}