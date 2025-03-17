package com.Smarth.ScholarHub.Repositories;

import com.Smarth.ScholarHub.Models.SubjectNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubjectNewRepository  extends JpaRepository<SubjectNew, UUID> {

    List<SubjectNew> findByUserId(UUID userId);

}
