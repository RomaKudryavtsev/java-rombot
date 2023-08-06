package com.rombot.rombot.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document("raw_contacts")
public class SourceContact {
    @Id
    ObjectId id;
    String name;
    String phone;
}
