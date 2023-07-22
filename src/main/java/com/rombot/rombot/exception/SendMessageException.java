package com.rombot.rombot.exception;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendMessageException extends RuntimeException {
    String phone;
    String message;

    public SendMessageException(String phone, String message) {
        super(String.format("ERROR: %s for CONTACT: %s", message, phone));
    }
}
