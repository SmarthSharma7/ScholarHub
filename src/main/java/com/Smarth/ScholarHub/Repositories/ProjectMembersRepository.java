package com.Smarth.ScholarHub.Repositories;

import com.Smarth.ScholarHub.Models.Project;
import com.Smarth.ScholarHub.Models.ProjectMembers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectMembersRepository extends JpaRepository<ProjectMembers, UUID> {

    List<ProjectMembers> findByProjectId(UUID projectId); // Fixed method signature

    @Query("SELECT pm.project FROM ProjectMembers pm WHERE pm.user.id = :userId")
    List<Project> findProjectsByUserId(@Param("userId") UUID userId); // Fixed method signature

    boolean existsByProjectIdAndUserId(UUID projectId, UUID userId);
}