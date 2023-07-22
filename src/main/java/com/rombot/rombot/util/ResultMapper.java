package com.rombot.rombot.util;

import com.rombot.rombot.dto.ResultDto;
import com.rombot.rombot.entity.Result;

public class ResultMapper {
    public static ResultDto entityToDto(Result entity) {
        return ResultDto.builder()
                .phone(entity.getPhone())
                .sent(entity.getSent())
                .build();
    }
}
