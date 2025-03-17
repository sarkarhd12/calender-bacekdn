package com.calender.apis.controller;


import com.calender.apis.enums.ActionType;
import com.calender.apis.model.EventMetadata;
import com.calender.apis.service.EventAdapter;
import com.calender.apis.service.EventsMetaDataService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/events")
public class EventMetaDataController {

    @Autowired
    private EventsMetaDataService eventsMetaDataService;

    @Autowired
    private EventAdapter eventAdapter;

    @PostMapping("/create-metadata")
    public EventMetadata createEventMetaData(@RequestBody EventMetadata eventMetadata){
        EventMetadata metadata = eventsMetaDataService.createEventMetaData(eventMetadata);
        eventAdapter.processEvent(metadata, ActionType.CREATE);
        return metadata;
    }

    @GetMapping("/metadata/{id}")
    public ResponseEntity<EventMetadata> getEventMetaDataById(@PathVariable Long id) {
        Optional<EventMetadata> eventMetadata = eventsMetaDataService.getEventMetaDataById(id);
        return eventMetadata.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/getAll-metaData")
    public ResponseEntity<List<EventMetadata>> getAllEventMetadata() {
        List<EventMetadata> metadataList = eventsMetaDataService.getAllEventMetadata();
        return ResponseEntity.ok(metadataList);
    }

    @GetMapping("/get-metadata/id/{id}")
    public ResponseEntity<EventMetadata> getMetadataById(@PathVariable Long id){
        Optional<EventMetadata> eventMetadata = eventsMetaDataService.getEventMetaDataById(id);
        return eventMetadata.map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.notFound().build());
    }


    @PutMapping("/update-metadata/{id}")
    public ResponseEntity<EventMetadata> updateMetadata(@PathVariable Long id, @RequestBody EventMetadata updatedMetadata) {
        EventMetadata updated = eventsMetaDataService.updateEventMetadata(id, updatedMetadata);
        eventAdapter.processEvent(updated, ActionType.UPDATE);
        return ResponseEntity.ok(updated);
    }

    @Transactional
    @DeleteMapping("/delete-metadata/{id}")
    public ResponseEntity<String> deleteMetadata(@PathVariable Long id) {
        Optional<EventMetadata> metadata = eventsMetaDataService.getEventMetaDataById(id);

        if (metadata.isPresent()) {
            eventAdapter.processEvent(metadata.get(), ActionType.DELETE);
            return ResponseEntity.ok("Event metadata and associated events deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
