package com.smarthome.notification.legacy;

/**
 * Сторонний класс драйвера GSM-модема. Нельзя изменять!
 */
public class LegacyGsmModem {

    public GsmSendResult transmitSms(int countryCode, long phoneNumber, byte[] payloadPdu) {
        if (phoneNumber <= 0) {
            return new GsmSendResult(400, "BAD_REQUEST: Invalid phone number format");
        }
        if (payloadPdu == null || payloadPdu.length == 0) {
            return new GsmSendResult(400, "BAD_REQUEST: Empty PDU payload");
        }

        return new GsmSendResult(0, "OK: SMS delivered via GSM tower");
    }
}