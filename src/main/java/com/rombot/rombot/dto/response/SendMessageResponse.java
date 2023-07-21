package com.rombot.rombot.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendMessageResponse {
    String messaging_product;
    ResponseContact[] contacts;
    ResponseMessage[] messages;
}
