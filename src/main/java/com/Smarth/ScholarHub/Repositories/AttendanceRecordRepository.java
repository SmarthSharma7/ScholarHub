package com.Smarth.ScholarHub.Repositories;

import com.Smarth.ScholarHub.Models.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, UUID> {

    List<AttendanceRecord> findByUserIdAndSubjectNewId(UUID userId, UUID subjectId);

    Optional<AttendanceRecord> findBySubjectNewIdAndDate(UUID subjectId, LocalDate date);

    List<AttendanceRecord> findBySubjectNewId(UUID subjectId);

}