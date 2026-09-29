package com.smarthome.notification.legacy;

public class GsmSendResult {
    private final int statusCode;
    private final String statusMessage;

    public GsmSendResult(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public boolean isSuccess() {
        return statusCode == 0;
    }
}