package com.calender.apis.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Events {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
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


    @Column(name = "attendee_email")
    private String attendeesEmails;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "event_metadata_id", referencedColumnName = "metadata_id")
    private EventMetadata eventMetadata;


}
