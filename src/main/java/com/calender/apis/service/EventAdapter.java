package com.calender.apis.service;

import com.calender.apis.enums.ActionType;
import com.calender.apis.enums.RecurrenceType;
import com.calender.apis.model.EventMetadata;
import com.calender.apis.model.Events;
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
    EventsMetaDataService eventsMetaDataService;
    @Async
    public HttpStatus processEvent(EventMetadata payload, ActionType actionType){
        switch (actionType){
            case CREATE -> createEvents(payload);
            case UPDATE -> updateEvents(payload);
            case DELETE -> deleteEvents(payload);
        }
        return HttpStatus.OK;
    }

//    @Transactional
//    private void createEvents(EventMetadata payload) {
//        ZonedDateTime startDate = payload.getStartDate();
//        ZonedDateTime endDate = payload.getEndDate();
//        RecurrenceType recurrenceType = payload.getRecurrenceType();
//        List<LocalDate> eventDates = new ArrayList<>();
//
//        switch (recurrenceType) {
//            case NONE:
//                eventDates = startDate.toLocalDate()
//                        .datesUntil(endDate.toLocalDate().plusDays(1))
//                        .collect(Collectors.toList());
//                break;
//
//            case DAILY:
//                List<DayOfWeek> exceptionDaysList = parseDays(payload.getExceptionDays());
//                eventDates = startDate.toLocalDate()
//                        .datesUntil(endDate.toLocalDate().plusDays(1))
//                        .filter(date -> !exceptionDaysList.contains(date.getDayOfWeek())) // Exclude exception days
//                        .collect(Collectors.toList());
//                break;
//
//            case WEEKLY:
//                List<DayOfWeek> meetingDaysList = parseDays(payload.getMeetingDays());
//                eventDates = startDate.toLocalDate()
//                        .datesUntil(endDate.toLocalDate().plusDays(1))
//                        .filter(date -> meetingDaysList.contains(date.getDayOfWeek())) // Only include meeting days
//                        .collect(Collectors.toList());
//                break;
//
//            case MONTHLY:
//                List<DayOfWeek> monthlyMeetingDaysList = parseDays(payload.getMeetingDays());
//                LocalDate current = startDate.toLocalDate();
//                while (!current.isAfter(endDate.toLocalDate())) {
//                    if (monthlyMeetingDaysList.contains(current.getDayOfWeek())) {
//                        eventDates.add(current);
//                    }
//                    current = current.plusDays(1); // Move to the next day
//                }
//                break;
//        }
//
//        // Create event objects and save
//        List<Events> eventsList = eventDates.stream().map(date -> {
//            Events event = new Events();
//            event.setTitle(payload.getTitle());
//            event.setAttendeesEmails(payload.getAttendeesEmails());
//            event.setDescription(payload.getDescription());
//            event.setVenue(payload.getVenue());
//            event.setStartDate(date.atStartOfDay(ZoneId.of("UTC")));
//            event.setEndDate(date.atStartOfDay(ZoneId.of("UTC")));
//            event.setStartTime(payload.getStartTime());
//            event.setEndTime(payload.getEndTime());
//            event.setEventMetadata(payload);
//            System.out.println("Created event: " + event);  // Log the created event object
//            return event;
//        }).collect(Collectors.toList());
//
//        System.out.println("Events to be saved: " + eventsList.size());
//
//        eventsService.createEvent(eventsList);
//    }
//


    @Transactional
    private void createEvents(EventMetadata payload) {
        // Fetch the managed EventMetadata from DB
        Optional<EventMetadata> managedMetadataOptional = eventsMetaDataService.findById(payload.getId());

        if (managedMetadataOptional.isEmpty()) {
            throw new IllegalArgumentException("EventMetadata not found in database: " + payload.getId());
        }

        EventMetadata managedMetadata = managedMetadataOptional.get();  // Extract from Optional

        ZonedDateTime startDate = managedMetadata.getStartDate();
        ZonedDateTime endDate = managedMetadata.getEndDate();
        RecurrenceType recurrenceType = managedMetadata.getRecurrenceType();
        List<LocalDate> eventDates = new ArrayList<>();

        switch (recurrenceType) {
            case NONE:
                eventDates = startDate.toLocalDate()
                        .plusDays(1)
                        .datesUntil(endDate.toLocalDate().plusDays(2))
                        .collect(Collectors.toList());
                break;
            case DAILY:
                List<DayOfWeek> exceptionDaysList = parseDays(managedMetadata.getExceptionDays());
                eventDates = startDate.toLocalDate()
                        .datesUntil(endDate.toLocalDate().plusDays(1))
                        .filter(date -> !exceptionDaysList.contains(date.getDayOfWeek()))
                        .collect(Collectors.toList());
                break;
            case WEEKLY:
                List<DayOfWeek> meetingDaysList = parseDays(managedMetadata.getMeetingDays());
                eventDates = startDate.toLocalDate()
                        .datesUntil(endDate.toLocalDate().plusDays(1))
                        .filter(date -> meetingDaysList.contains(date.getDayOfWeek()))
                        .collect(Collectors.toList());
                break;
            case MONTHLY:
                List<DayOfWeek> monthlyMeetingDaysList = parseDays(managedMetadata.getMeetingDays());
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
            event.setTitle(managedMetadata.getTitle());
            event.setAttendeesEmails(managedMetadata.getAttendeesEmails());
            event.setDescription(managedMetadata.getDescription());
            event.setVenue(managedMetadata.getVenue());
            event.setStartDate(date.atStartOfDay(ZoneId.of("UTC")));
            event.setEndDate(date.atStartOfDay(ZoneId.of("UTC")));
            event.setStartTime(managedMetadata.getStartTime());
            event.setEndTime(managedMetadata.getEndTime());
            event.setEventMetadata(managedMetadata);  // Use managed entity
            System.out.println("Created event: " + event);
            return event;
        }).collect(Collectors.toList());

        System.out.println("Events to be saved: " + eventsList.size());

        eventsService.createEvent(eventsList);
    }



//    @Transactional
//    public void updateEvents(EventMetadata payload) {
//        List<Events> existingEvents = eventsService.getEventsByMetadataId(payload.getId());
//
//        if (existingEvents.isEmpty()) {
//            throw new RuntimeException("No events found for metadata ID: " + payload.getId());
//        }
//
//        // Update event details
//        existingEvents.forEach(event -> {
//            event.setTitle(payload.getTitle());
//            event.setAttendeesEmails(payload.getAttendeesEmails());
//            event.setDescription(payload.getDescription());
//            event.setVenue(payload.getVenue());
//            event.setStartDate(payload.getStartDate());
//            event.setEndDate(payload.getEndDate());
//            event.setStartTime(payload.getStartTime());
//            event.setEndTime(payload.getEndTime());
//            event.setEventMetadata(payload);
//        });
//
//        // Save updated events
//        eventsService.updateEvents(existingEvents);
//    }


    @Transactional
    public void updateEvents(EventMetadata payload) {
        List<Events> existingEvents = eventsService.getEventsByMetadataId(payload.getId());

        if (existingEvents.isEmpty()) {
            throw new RuntimeException("No events found for metadata ID: " + payload.getId());
        }

        // Fetch the managed entity from DB to ensure it's attached
        EventMetadata managedMetadata = eventsMetaDataService.findById(payload.getId())
                .orElseThrow(() -> new RuntimeException("EventMetadata not found for ID: " + payload.getId()));

        existingEvents.forEach(event -> {
            event.setTitle(payload.getTitle());
            event.setAttendeesEmails(payload.getAttendeesEmails());
            event.setDescription(payload.getDescription());
            event.setVenue(payload.getVenue());
            event.setStartDate(payload.getStartDate());
            event.setEndDate(payload.getEndDate());
            event.setStartTime(payload.getStartTime());
            event.setEndTime(payload.getEndTime());

            // Set the managed entity instead of detached entity
            event.setEventMetadata(managedMetadata);
        });

        // Save updated events
        eventsService.updateEvents(existingEvents);
    }





    @Transactional
    private void deleteEvents(EventMetadata payload) {

        List<Events> eventsToDelete = eventsService.getEventsByMetadataId(payload.getId());

        // Delete all associated events
        if (!eventsToDelete.isEmpty()) {
            eventsService.deleteEventsByMetadataId(payload.getId());
        }

        // Delete the event metadata itself
        eventsMetaDataService.deleteMetadataById(payload.getId());
    }


    private List<DayOfWeek> parseDays(String days) {
        if (days == null || days.isEmpty()) return new ArrayList<>();
        return Arrays.stream(days.split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .map(DayOfWeek::valueOf)
                .collect(Collectors.toList());
    }

}
