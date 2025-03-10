package com.Smarth.ScholarHub.Repositories;

import com.Smarth.ScholarHub.Models.ProjectInvite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectInvitesRepository extends JpaRepository<ProjectInvite, UUID> {

    List<ProjectInvite> findByReceiverId(UUID receiverId);
    List<ProjectInvite> findBySenderIdOrReceiverId(UUID senderId, UUID receiverId);

}