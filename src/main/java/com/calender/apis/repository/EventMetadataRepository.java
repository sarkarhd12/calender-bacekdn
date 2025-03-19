package com.calender.apis.repository;

import com.calender.apis.model.EventMetadata;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository

public interface EventMetadataRepository extends JpaRepository<EventMetadata,Long> {

}
