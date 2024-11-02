package com.andersen.marketplace.repository;

import com.andersen.marketplace.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing users.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Finds a user by their name.
     *
     * @param name the name of the user
     * @return an Optional containing the User, if found
     */
    Optional<User> findByName(String name);
}
