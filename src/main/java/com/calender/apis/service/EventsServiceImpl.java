package com.calender.apis.service;

import com.calender.apis.dto.EventTimeDTO;
import com.calender.apis.model.Events;
import com.calender.apis.repository.EventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class EventsServiceImpl implements EventsService{

    @Autowired
    private EventsRepository eventsRepository;

    @Override
    public List<Events> createEvent(List<Events> events) {
        return eventsRepository.saveAll(events);
    }

    @Override
    public Optional<Events> getEventById(Long id) {
        return eventsRepository.findById(id);
    }

    @Override
    public List<Events> getEventsByDate(LocalDate date) {
        return eventsRepository.findByStartDate(date.toString()); // Converts LocalDate to "YYYY-MM-DD"    }
    }
    @Override
    public Events updateEvent(Long id, Events updatedEvent) {
        return eventsRepository.findById(id).map(event -> {
            event.setTitle(updatedEvent.getTitle());
            event.setDescription(updatedEvent.getDescription());
            event.setStartTime(updatedEvent.getStartTime());
            event.setEndTime(updatedEvent.getEndTime());
            event.setVenue(updatedEvent.getVenue());
            event.setAttendeesEmails(updatedEvent.getAttendeesEmails());

            return eventsRepository.save(event);
        }).orElseThrow(() -> new RuntimeException("Event not found with ID: " + id));
    }

    @Override
    public void deleteEvent(Long id) {
        eventsRepository.deleteById(id);
    }

//    @Override
    public List<EventTimeDTO> getEventTimesByDate(LocalDate date) {

        return eventsRepository.findEventTimesByDate(date)
                .stream()
                .map(obj -> new EventTimeDTO((Long) obj[0], (ZonedDateTime) obj[1], (ZonedDateTime) obj[2])) // Extract ID
                .collect(Collectors.toList());
    }


    public List<Events> getEventsByMetadataId(Long metadataId) {
        return eventsRepository.findByEventMetadataId(metadataId);
    }

    public void updateEvents(List<Events> events) {
        eventsRepository.saveAll(events);
    }

    public void deleteEventsByMetadataId(Long metadataId) {
        eventsRepository.deleteByEventMetadataId(metadataId);
    }

}
