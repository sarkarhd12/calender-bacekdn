package com.calender.apis.dto;

import com.calender.apis.enums.RecurrenceType;
import lombok.*;

import java.time.DayOfWeek;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventMetadataDto {
    private Long id;
    private Long eventId;
    private RecurrenceType recurrenceType;
    private List<DayOfWeek> meetingDays;
    private List<DayOfWeek> exceptionDays;
    private boolean customDays;
}
