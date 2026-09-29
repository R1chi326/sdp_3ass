package com.smarthome.notification;

import com.smarthome.notification.abstraction.DeviceStatusNotification;
import com.smarthome.notification.abstraction.SecurityAlertNotification;
import com.smarthome.notification.abstraction.SmartHomeNotification;
import com.smarthome.notification.adapter.GsmSmsChannelAdapter;
import com.smarthome.notification.channel.EmailNotificationChannel;
import com.smarthome.notification.channel.NotificationChannel;
import com.smarthome.notification.channel.PushNotificationChannel;
import com.smarthome.notification.legacy.LegacyGsmModem;
import com.smarthome.notification.selector.DynamicChannelSelector;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== SMART HOME NOTIFICATION SYSTEM ===");

        // 1. Инициализируем каналы
        NotificationChannel pushChannel = new PushNotificationChannel();
        NotificationChannel emailChannel = new EmailNotificationChannel();

        // Оборачиваем несовместимый Legacy GSM-модем через Адаптер
        LegacyGsmModem gsmModem = new LegacyGsmModem();
        NotificationChannel gsmSmsChannel = new GsmSmsChannelAdapter(gsmModem);

        // 2. Настраиваем DynamicChannelSelector
        DynamicChannelSelector selector = new DynamicChannelSelector();
        selector.registerChannel(pushChannel);
        selector.registerChannel(emailChannel);
        selector.registerChannel(gsmSmsChannel);

        try {
            // Сценарий 1: Критическое событие при отсутствии интернета -> выбирается GSM SMS
            System.out.println("\n--- Scenario 1: Internet is DOWN, Security Alert Triggered ---");
            NotificationChannel selected1 = selector.selectChannel(false, true);
            SmartHomeNotification alert = new SecurityAlertNotification(selected1, "Front Door Sensor");
            alert.notifyUser("+77011234567", "Unauthorized entry detected!");

            // Сценарий 2: Обычное событие, интернет есть -> выбирается EMAIL
            System.out.println("\n--- Scenario 2: Internet is UP, Routine Device Status ---");
            NotificationChannel selected2 = selector.selectChannel(true, false);
            SmartHomeNotification status = new DeviceStatusNotification(selected2, "Washing Machine");
            status.notifyUser("user@smarthome.io", "Laundry cycle finished successfully.");

        } catch (Exception e) {
            System.err.println("Notification delivery failed: " + e.getMessage());
        }
    }
}