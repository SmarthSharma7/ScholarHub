package com.Smarth.ScholarHub.Repositories;

import com.Smarth.ScholarHub.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query(value = "SELECT * FROM user_details WHERE " +
            "(LOWER(unaccent(name)) LIKE LOWER(unaccent(CONCAT('%', :query, '%'))) OR " +
            "LOWER(unaccent(email)) LIKE LOWER(unaccent(CONCAT('%', :query, '%'))) OR " +
            "LOWER(unaccent(skills)) LIKE LOWER(unaccent(CONCAT('%', :query, '%'))))",
            nativeQuery = true)
    List<User> searchUsers(@Param("query") String query);

}