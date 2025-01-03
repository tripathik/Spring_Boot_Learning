package com.intelliguru.simplora.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "config")
public class ConfigEntity {
    private String key;
    private String value;
}
