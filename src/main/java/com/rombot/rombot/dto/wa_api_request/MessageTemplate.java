package com.rombot.rombot.dto.wa_api_request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageTemplate {
    String name;
    MessageLanguage language;
}
