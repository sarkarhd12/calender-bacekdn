package com.calender.apis.controller;


import com.calender.apis.dto.EventTimeDTO;
import com.calender.apis.model.Events;
import com.calender.apis.service.EventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/events")
public class EventsController {

    @Autowired
    private EventsService eventsService;

    @PostMapping("/create")
    public List<Events> createEvent(@RequestBody List<Events> events){
        return eventsService.createEvent(events);
    }

    @GetMapping("/getEventById/{id}")
    public ResponseEntity<Events> getEventById(@PathVariable Long id) {
        Optional<Events> event = eventsService.getEventById(id);
        return event.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/updateEvent/{id}")
    public ResponseEntity<Events> updateEvent(@PathVariable Long id, @RequestBody Events updatedEvent) {
        try {
            return ResponseEntity.ok(eventsService.updateEvent(id, updatedEvent));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteEvent/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long id) {
        eventsService.deleteEvent(id);
        return ResponseEntity.ok("Event deleted successfully.");
    }

    @GetMapping("/by-date")
    public ResponseEntity<List<Events>> getEventsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(eventsService.getEventsByDate(date));
    }

    @GetMapping("/by-date/times")
    public ResponseEntity<List<EventTimeDTO>> getEventTimesByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(eventsService.getEventTimesByDate(date));
    }

}
