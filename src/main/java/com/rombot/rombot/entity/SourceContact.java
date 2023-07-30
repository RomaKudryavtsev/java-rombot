package com.rombot.rombot.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document("contacts")
public class SourceContact {
    @Id
    ObjectId id;
    String name;
    String phone;
    Boolean readyToVote;
    String address;
    String district;
    LocalDateTime saved;
}
