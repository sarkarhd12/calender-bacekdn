package com.calender.apis.dto;

import lombok.Data;

@Data
public class EventWithMetadataDto {
    private EventsDto event;
    private EventMetadataDto metadata;
}
