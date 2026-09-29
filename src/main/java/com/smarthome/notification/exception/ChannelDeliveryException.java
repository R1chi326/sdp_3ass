package com.smarthome.notification.exception;

public class ChannelDeliveryException extends Exception {
    public ChannelDeliveryException(String message) {
        super(message);
    }

    public ChannelDeliveryException(String message, Throwable cause) {
        super(message, cause);
    }
}