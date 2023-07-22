package com.rombot.rombot.exception;

public class SendMessageException extends RuntimeException {
    String phone;
    String message;

    public SendMessageException(String phone, String message) {
        super(String.format("ERROR: %s for CONTACT: %s", message, phone));
    }
}
