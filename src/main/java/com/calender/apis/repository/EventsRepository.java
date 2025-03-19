package com.calender.apis.repository;

import com.calender.apis.model.Events;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface EventsRepository extends JpaRepository<Events, Long> {

    @Query("SELECT e FROM Events e WHERE CAST(e.startDate AS string) LIKE CONCAT(:date, '%')")
    List<Events> findByStartDate(@Param("date") String date);

    @Query("SELECT e.id,e.startTime, e.endTime FROM Events e WHERE FUNCTION('DATE', e.startDate) = :date")
    List<Object[]> findEventTimesByDate(@Param("date") LocalDate date);

//    void deleteByMetadataId(Long metadataId);

    List<Events> findByEventMetadataId(Long metadataId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Events e WHERE e.eventMetadata.id = :metadataId")
    void deleteByEventMetadataId(Long metadataId);



}