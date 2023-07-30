package com.rombot.rombot.dto.ultramsg_request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UltraMsgRequest {
    @NotBlank
    String token;
    @NotBlank
    String to;
    @NotBlank
    String body;
}
