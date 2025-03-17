package com.calender.apis.dto;

import lombok.*;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventsDto {
    private Long id;
    private String title;
    private String description;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private String venue;
    private boolean allDay;
    private List<String> attendeesEmails;
    private List<EventMetadataDto> eventMetadata;
}