package com.mklinga.reflekt.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class JournalEntryDto {
    private UUID id;

    private String mood;

    private String title;

    private String entry;
}
