package com.rombot.rombot.util;

import com.rombot.rombot.dto.ResultDto;
import com.rombot.rombot.entity.Result;
import com.rombot.rombot.entity.SourceContact;

import java.time.LocalDateTime;

public class ResultMapper {
    public static ResultDto entityToDto(Result entity) {
        return ResultDto.builder()
                .phone(entity.getPhone())
                .sent(entity.getSent())
                .build();
    }

    public static Result sourceContactToResult(SourceContact contact) {
        Result result = new Result();
        result.setPhone(contact.getPhone());
        result.setName(contact.getName());
        result.setSent(LocalDateTime.now());
        return result;
    }
}
