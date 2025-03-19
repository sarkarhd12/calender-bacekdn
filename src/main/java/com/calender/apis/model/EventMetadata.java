package com.calender.apis.model;

import com.calender.apis.enums.RecurrenceType;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "event_metadata")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metadata_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_time", nullable = false)
    private ZonedDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private ZonedDateTime endTime;

    @Column(name = "start_date", nullable = false)
    private ZonedDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private ZonedDateTime endDate;

    @Column(name = "venue")
    private String venue;

    @Column(name = "all_day", nullable = false)
    private boolean allDay;

    //@ElementCollection
    @Column(name = "attendee_email")
    private String attendeesEmails;

    @Column(name = "ip_address")
    private String ipAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "recurrence_type")
    private RecurrenceType recurrenceType;


    @Column(name = "meeting_day")
    private String meetingDays;

    /*@ElementCollection(targetClass = DayOfWeek.class)
    @Enumerated(EnumType.STRING)*/
    @Column(name = "exception_day")
    private String exceptionDays;



}
