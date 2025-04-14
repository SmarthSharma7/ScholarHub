package com.Smarth.ScholarHub.Services;

import com.Smarth.ScholarHub.DTOs.SubjectRecord;
import com.Smarth.ScholarHub.DTOs.SubjectResponse;
import com.Smarth.ScholarHub.Models.AttendanceRecord;
import com.Smarth.ScholarHub.Models.SubjectNew;
import com.Smarth.ScholarHub.Models.User;
import com.Smarth.ScholarHub.Repositories.AttendanceRecordRepository;
import com.Smarth.ScholarHub.Repositories.SubjectNewRepository;
import com.Smarth.ScholarHub.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SubjectNewService {

    private final SubjectNewRepository subjectNewRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;
    private final UserRepository userRepository;

    @Autowired
    public SubjectNewService(final SubjectNewRepository subjectNewRepository,
                              final AttendanceRecordRepository attendanceRecordRepository,
                              final UserRepository userRepository) {
        this.subjectNewRepository = subjectNewRepository;
        this.attendanceRecordRepository = attendanceRecordRepository;
        this.userRepository = userRepository;
    }

    public UUID addSubject(UUID userId, String subjectName) {
        SubjectNew subjectNew = new SubjectNew();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        subjectNew.setUser(user);
        subjectNew.setName(subjectName);
        subjectNewRepository.save(subjectNew);
        return subjectNew.getId();
    }

    public void deleteSubject(UUID subjectId) {
        SubjectNew subjectNew = subjectNewRepository.findById(subjectId).orElseThrow(() -> new RuntimeException("Subject not found"));
        subjectNewRepository.delete(subjectNew);
    }

    public List<SubjectResponse> getAllSubjects(UUID userId) {
        List<SubjectNew> subjects = subjectNewRepository.findByUserId(userId);
        List<SubjectResponse> subjectResponses = new ArrayList<>();

        for (SubjectNew subject : subjects) {
            List<AttendanceRecord> attendanceRecords = attendanceRecordRepository.findByUserIdAndSubjectNewId(userId, subject.getId());

            int totalClasses = attendanceRecords.size();
            int attendedClasses = (int) attendanceRecords.stream()
                    .filter(record -> "present".equalsIgnoreCase(record.getStatus()))
                    .count();

            double attendedPercentage = totalClasses > 0 ? (attendedClasses * 100.0) / totalClasses : 0.0;

            List<SubjectRecord> subjectRecordList = new ArrayList<>();
            for (AttendanceRecord attendanceRecord : attendanceRecords) {
                SubjectRecord subjectRecord = new SubjectRecord();
                subjectRecord.setSubjectId(subject.getId());
                subjectRecord.setDate(attendanceRecord.getDate());
                subjectRecord.setStatus(attendanceRecord.getStatus());
                subjectRecord.setNote(attendanceRecord.getNote());
                subjectRecordList.add(subjectRecord);
            }

            SubjectResponse response = SubjectResponse.builder()
                    .id(subject.getId())
                    .name(subject.getName())
                    .totalClasses(totalClasses)
                    .attendedClasses(attendedClasses)
                    .attendedPercentage(attendedPercentage)
                    .createdAt(subject.getCreatedAt())
                    .subjectRecord(subjectRecordList)
                    .build();

            subjectResponses.add(response);
        }

        return subjectResponses;
    }

    public List<SubjectRecord> updateSubject(List<SubjectRecord> subjectRecordList) {
        SubjectNew subjectToBeUpdated = subjectNewRepository.findById(subjectRecordList.getFirst().getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        for (SubjectRecord subjectRecord : subjectRecordList) {
            Optional<AttendanceRecord> oneRecord = attendanceRecordRepository.findBySubjectNewIdAndDate(subjectToBeUpdated.getId(), subjectRecord.getDate());
            if (oneRecord.isPresent()) {
                AttendanceRecord attendanceRecord = oneRecord.get();
                if (StringUtils.hasText(subjectRecord.getStatus())) {
                    attendanceRecord.setStatus(subjectRecord.getStatus());
                    attendanceRecord.setNote(subjectRecord.getNote());
                    attendanceRecordRepository.save(attendanceRecord);
                } else {
                    attendanceRecordRepository.delete(attendanceRecord);
                }
            } else {
                AttendanceRecord newAttendanceRecord = new AttendanceRecord();
                newAttendanceRecord.setDate(subjectRecord.getDate());
                newAttendanceRecord.setStatus(subjectRecord.getStatus());
                newAttendanceRecord.setNote(subjectRecord.getNote());
                newAttendanceRecord.setUser(subjectToBeUpdated.getUser());
                newAttendanceRecord.setSubjectNew(subjectToBeUpdated);
                attendanceRecordRepository.save(newAttendanceRecord);
            }
        }
        List<AttendanceRecord> attendanceRecords = attendanceRecordRepository.findBySubjectNewId(subjectToBeUpdated.getId());
        List<SubjectRecord> subjectRecords = new ArrayList<>();
        for (AttendanceRecord attendanceRecord : attendanceRecords) {
            SubjectRecord subjectRecord = new SubjectRecord();
            subjectRecord.setSubjectId(attendanceRecord.getSubjectNew().getId());
            subjectRecord.setDate(attendanceRecord.getDate());
            subjectRecord.setStatus(attendanceRecord.getStatus());
            subjectRecord.setNote(attendanceRecord.getNote());
            subjectRecords.add(subjectRecord);
        }
        return subjectRecords;
    }

}