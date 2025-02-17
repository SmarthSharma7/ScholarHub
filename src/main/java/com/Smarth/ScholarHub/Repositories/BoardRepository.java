
package com.Smarth.ScholarHub.Repositories;

import com.Smarth.ScholarHub.Models.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BoardRepository extends JpaRepository<Board, UUID> {

    List<Board> findByUserId(UUID userId);// Get all boards for a user

}