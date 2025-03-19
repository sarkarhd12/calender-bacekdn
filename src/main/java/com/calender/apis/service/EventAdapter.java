package com.calender.apis.service;

import com.calender.apis.enums.ActionType;
import com.calender.apis.enums.RecurrenceType;
import com.calender.apis.model.EventMetadata;
import com.calender.apis.model.Events;
import com.calender.apis.repository.EventMetadataRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Transactional
public class EventAdapter {
    @Autowired
    private EventsService eventsService;

    @Autowired
    private EntityManager em;


    //@Async
    public HttpStatus processEvent(EventMetadata payload, ActionType actionType){
        switch (actionType){
            case CREATE -> createEvents(payload);
            case UPDATE -> updateEvents(payload);
            case DELETE -> deleteEvents(payload);
        }
        return HttpStatus.OK;
    }



  //  @Transactional
    private void createEvents(EventMetadata payload) {


        ZonedDateTime startDate = payload.getStartDate().withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
        ZonedDateTime endDate = payload.getEndDate().withZoneSameInstant(ZoneId.of("Asia/Kolkata"));


        RecurrenceType recurrenceType = payload.getRecurrenceType();
        List<LocalDate> eventDates = new ArrayList<>();

        switch (recurrenceType) {
            case NONE:
                eventDates = startDate.toLocalDate()
//                        .plusDays(1)
                        .datesUntil(endDate.toLocalDate().plusDays(1))
                        .collect(Collectors.toList());
                break;
            case DAILY:
                List<DayOfWeek> exceptionDaysList = parseDays(payload.getExceptionDays());
                eventDates = startDate.toLocalDate()
                        .datesUntil(endDate.toLocalDate().plusDays(1))
                        .filter(date -> !exceptionDaysList.contains(date.getDayOfWeek()))
                        .collect(Collectors.toList());
                break;
            case WEEKLY:
                List<DayOfWeek> meetingDaysList = parseDays(payload.getMeetingDays());
                eventDates = startDate.toLocalDate()
                        .datesUntil(endDate.toLocalDate().plusDays(1))
                        .filter(date -> meetingDaysList.contains(date.getDayOfWeek()))
                        .collect(Collectors.toList());
                break;
            case MONTHLY:
                List<DayOfWeek> monthlyMeetingDaysList = parseDays(payload.getMeetingDays());
                LocalDate current = startDate.toLocalDate();
                while (!current.isAfter(endDate.toLocalDate())) {
                    if (monthlyMeetingDaysList.contains(current.getDayOfWeek())) {
                        eventDates.add(current);
                    }
                    current = current.plusDays(1);
                }
                break;
        }

        List<Events> eventsList = eventDates.stream().map(date -> {
            Events event = new Events();
            event.setTitle(payload.getTitle());
            event.setAttendeesEmails(payload.getAttendeesEmails());
            event.setDescription(payload.getDescription());
            event.setVenue(payload.getVenue());
            event.setStartDate(date.atStartOfDay(ZoneId.of("UTC")));
            event.setEndDate(date.atStartOfDay(ZoneId.of("UTC")));
            event.setStartTime(payload.getStartTime());
            event.setEndTime(payload.getEndTime());
            event.setEventMetadata(payload);  // Use managed entity
            System.out.println("Created event: " + event);
            return event;
        }).collect(Collectors.toList());

        System.out.println("Events to be saved: " + eventsList.size());

        eventsService.createEvent(eventsList);
    }




   // @Transactional
    public void updateEvents(EventMetadata payload) {
        List<Events> existingEvents = eventsService.getEventsByMetadataId(payload.getId());

        if (existingEvents.isEmpty()) {
            throw new RuntimeException("No events found for metadata ID: " + payload.getId());
        }


        existingEvents.forEach(event -> {

            ZonedDateTime existingStartDate = event.getStartDate();
            ZonedDateTime existingEndDate = event.getEndDate();

            event.setTitle(payload.getTitle());
            event.setAttendeesEmails(payload.getAttendeesEmails());
            event.setDescription(payload.getDescription());
            event.setVenue(payload.getVenue());

            event.setStartDate(existingStartDate);
            event.setEndDate(existingEndDate);

            event.setStartTime(payload.getStartTime());
            event.setEndTime(payload.getEndTime());

            // Set the managed entity instead of detached entity
            event.setEventMetadata(payload);
        });

        // Save updated events
        eventsService.updateEvents(existingEvents);
    }






    private void deleteEvents(EventMetadata payload) {

        List<Events> eventsToDelete = eventsService.getEventsByMetadataId(payload.getId());

        // Delete all associated events
        if (!eventsToDelete.isEmpty()) {
            System.out.println("Deleting associated events...");
            eventsService.deleteEventsByMetadataId(payload.getId());
        }

    }


    private List<DayOfWeek> parseDays(String days) {
        if (days == null || days.isEmpty()) return new ArrayList<>();
        return Arrays.stream(days.split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .map(DayOfWeek::valueOf)
                .collect(Collectors.toList());
    }

    public void deleteAllEvents(EventMetadata metadata) {
        List<Events> eventsToDelete = eventsService.getEventsByMetadataId(metadata.getId());

        // Delete all associated events
        if (!eventsToDelete.isEmpty()) {
            System.out.println("Deleting associated events...");
            eventsService.deleteEventsByMetadataId(metadata.getId());
        }
    }
}
