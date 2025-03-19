package com.calender.apis.service;

import com.calender.apis.enums.ActionType;
import com.calender.apis.model.EventMetadata;
import com.calender.apis.model.Events;
import com.calender.apis.repository.EventMetadataRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.event.spi.EventManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EventsMetaDataService {

    @Autowired
    private EventMetadataRepository eventMetadataRepository;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private EventAdapter eventAdapter;

   public EventMetadata createEventMetaData(EventMetadata eventMetadata){
       EventMetadata metadata = eventMetadataRepository.save(eventMetadata);

       eventAdapter.processEvent(metadata, ActionType.CREATE);

   return metadata;

    }


    public Optional<EventMetadata> getEventMetaDataById(Long id) {
        return eventMetadataRepository.findById(id);
    }

    public List<EventMetadata> getAllEventMetadata() {

       return eventMetadataRepository.findAll();
    }

    public void deleteMetadata(EventMetadata eventMetadata) {
        Optional<EventMetadata> metadataOpt = eventMetadataRepository.findById(eventMetadata.getId());

        eventAdapter.processEvent(eventMetadata, ActionType.DELETE);
      deleteMetadataOnly(metadataOpt.get());


    }
    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void deleteMetadataOnly(EventMetadata eventMetadata){
        eventMetadataRepository.deleteById(eventMetadata.getId());
    }

    @Transactional
    public EventMetadata updateEventMetadata(Long id, EventMetadata updatedMetadata) {
        EventMetadata metadata = eventMetadataRepository.findById(id)
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

        eventAdapter.processEvent(metadata, ActionType.UPDATE);


        return metadata;
    }


    public Optional<EventMetadata> findById(Long id) {
        return eventMetadataRepository.findById(id);
    }
}
