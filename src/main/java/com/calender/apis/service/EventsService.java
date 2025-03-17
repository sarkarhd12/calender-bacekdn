package com.calender.apis.service;

import com.calender.apis.dto.EventTimeDTO;
import com.calender.apis.model.Events;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventsService {
    List<Events> createEvent(List<Events> events);

    Optional<Events> getEventById(Long id);

    List<Events> getEventsByDate(LocalDate events);

    Events updateEvent(Long id, Events updatedEvent);

    void deleteEvent(Long id);

    List<EventTimeDTO> getEventTimesByDate(LocalDate date);

    List<Events> getEventsByMetadataId(Long metadataId);

    void updateEvents(List<Events> events);

    void deleteEventsByMetadataId(Long metadataId);

}
