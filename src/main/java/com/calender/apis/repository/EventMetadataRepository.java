package com.calender.apis.repository;

import com.calender.apis.model.EventMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface EventMetadataRepository extends JpaRepository<EventMetadata,Long> {
//    List<EventMetadata> findByEventId(Long eventId);
}
