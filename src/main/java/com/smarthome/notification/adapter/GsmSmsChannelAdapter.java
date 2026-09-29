package com.smarthome.notification.adapter;

import com.smarthome.notification.channel.NotificationChannel;
import com.smarthome.notification.exception.ChannelDeliveryException;
import com.smarthome.notification.legacy.GsmSendResult;
import com.smarthome.notification.legacy.LegacyGsmModem;

import java.nio.charset.StandardCharsets;

public class GsmSmsChannelAdapter implements NotificationChannel {

    private final LegacyGsmModem gsmModem;

    public GsmSmsChannelAdapter(LegacyGsmModem gsmModem) {
        this.gsmModem = gsmModem;
    }

    @Override
    public void send(String recipient, String header, String body) throws ChannelDeliveryException {
        try {
            ParsedPhone phone = parsePhone(recipient);

            String fullText = "[" + header + "] " + body;
            byte[] payload = fullText.getBytes(StandardCharsets.UTF_8);

            GsmSendResult result = gsmModem.transmitSms(phone.countryCode, phone.number, payload);

            if (!result.isSuccess()) {
                throw new ChannelDeliveryException(
                        "GSM Transmission Failed [Code " + result.getStatusCode() + "]: " + result.getStatusMessage()
                );
            }

            System.out.println("[SMS-GSM] Sent successfully to " + recipient);

        } catch (IllegalArgumentException e) {
            throw new ChannelDeliveryException("Invalid phone number format for GSM: " + recipient, e);
        } catch (Exception e) {
            if (e instanceof ChannelDeliveryException) {
                throw (ChannelDeliveryException) e;
            }
            throw new ChannelDeliveryException("Unexpected failure in GSM adapter", e);
        }
    }

    @Override
    public String getChannelType() {
        return "SMS";
    }

    private ParsedPhone parsePhone(String phoneStr) {
        if (phoneStr == null || !phoneStr.startsWith("+")) {
            throw new IllegalArgumentException("Phone number must start with '+'");
        }
        String clean = phoneStr.substring(1).replaceAll("\\s+", "");
        if (clean.length() < 10) {
            throw new IllegalArgumentException("Phone number too short");
        }
        int countryCode = Integer.parseInt(clean.substring(0, 1));
        long number = Long.parseLong(clean.substring(1));
        return new ParsedPhone(countryCode, number);
    }

    private static class ParsedPhone {
        final int countryCode;
        final long number;

        ParsedPhone(int countryCode, long number) {
            this.countryCode = countryCode;
            this.number = number;
        }
    }
}