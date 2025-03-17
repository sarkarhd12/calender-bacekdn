package com.calender.apis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;


@Data
@AllArgsConstructor
public class EventTimeDTO {
    private Long id;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
}
