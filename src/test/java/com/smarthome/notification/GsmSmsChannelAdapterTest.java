package com.smarthome.notification;

import com.smarthome.notification.adapter.GsmSmsChannelAdapter;
import com.smarthome.notification.exception.ChannelDeliveryException;
import com.smarthome.notification.legacy.GsmSendResult;
import com.smarthome.notification.legacy.LegacyGsmModem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GsmSmsChannelAdapterTest {

    // Заглушка для имитации успешной отправки GSM
    private static class SuccessGsmModem extends LegacyGsmModem {
        @Override
        public GsmSendResult transmitSms(int countryCode, long phoneNumber, byte[] payloadPdu) {
            return new GsmSendResult(0, "OK");
        }
    }

    // Заглушка для имитации ошибки GSM
    private static class FailingGsmModem extends LegacyGsmModem {
        @Override
        public GsmSendResult transmitSms(int countryCode, long phoneNumber, byte[] payloadPdu) {
            return new GsmSendResult(500, "Tower Connection Failed");
        }
    }

    @Test
    void testSendSuccess() {
        LegacyGsmModem modem = new SuccessGsmModem();
        GsmSmsChannelAdapter adapter = new GsmSmsChannelAdapter(modem);
        assertDoesNotThrow(() -> adapter.send("+77011234567", "Alert", "Test message"));
    }

    @Test
    void testSendFailureThrowsException() {
        LegacyGsmModem modem = new FailingGsmModem();
        GsmSmsChannelAdapter adapter = new GsmSmsChannelAdapter(modem);
        assertThrows(ChannelDeliveryException.class, () -> adapter.send("+77011234567", "Alert", "Test message"));
    }
}