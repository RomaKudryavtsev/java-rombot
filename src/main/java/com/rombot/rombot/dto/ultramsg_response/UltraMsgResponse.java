package com.rombot.rombot.dto.ultramsg_response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UltraMsgResponse {
    String sent;
    String message;
    Long id;
}
