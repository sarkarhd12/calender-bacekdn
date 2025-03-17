package com.calender.apis.service;

import com.calender.apis.model.EventMetadata;
import com.calender.apis.repository.EventMetadataRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventsMetaDataService {

    @Autowired
    private EventMetadataRepository eventMetadataRepository;

   public EventMetadata createEventMetaData(EventMetadata eventMetadata){
       return eventMetadataRepository.save(eventMetadata);

    }

    public Optional<EventMetadata> getEventMetaDataById(Long id) {
        return eventMetadataRepository.findById(id);
    }

    public List<EventMetadata> getAllEventMetadata() {
        return eventMetadataRepository.findAll();
    }

    public void deleteMetadataById(Long id) {
        eventMetadataRepository.deleteById(id);
    }

    @Transactional
    public EventMetadata updateEventMetadata(Long id, EventMetadata updatedMetadata) {
        return eventMetadataRepository.findById(id)
                .map(existingMetadata -> {
                    // Update fields
                    existingMetadata.setTitle(updatedMetadata.getTitle());
                    existingMetadata.setAttendeesEmails(updatedMetadata.getAttendeesEmails());
                    existingMetadata.setDescription(updatedMetadata.getDescription());
                    existingMetadata.setVenue(updatedMetadata.getVenue());
                    existingMetadata.setStartDate(updatedMetadata.getStartDate());
                    existingMetadata.setEndDate(updatedMetadata.getEndDate());
                    existingMetadata.setStartTime(updatedMetadata.getStartTime());
                    existingMetadata.setEndTime(updatedMetadata.getEndTime());
                    existingMetadata.setRecurrenceType(updatedMetadata.getRecurrenceType());
                    existingMetadata.setMeetingDays(updatedMetadata.getMeetingDays());
                    existingMetadata.setExceptionDays(updatedMetadata.getExceptionDays());

                    // Save updated metadata
                    EventMetadata savedMetadata = eventMetadataRepository.save(existingMetadata);

                    // Trigger event update in EventAdapter

                    return savedMetadata;
                })
                .orElseThrow(() -> new RuntimeException("Event metadata not found for ID: " + id));
    }


    public Optional<EventMetadata> findById(Long id) {
        return eventMetadataRepository.findById(id);
    }
}
