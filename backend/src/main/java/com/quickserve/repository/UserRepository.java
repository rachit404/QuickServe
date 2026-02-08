package com.quickserve.repository;

import com.quickserve.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * USER REPOSITORY
 * 
 * Data access interface for User entities.
 * 
 * Spring Data JPA automatically implements these methods!
 * You just define the method signature, and Spring generates the SQL.
 * 
 * INHERITED METHODS (from JpaRepository):
 * - save(User user)
 * - findById(Long id)
 * - findAll()
 * - delete(User user)
 * - count()
 * - and many more...
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find user by email address.
     * 
     * Used for:
     * - Login (find user to check password)
     * - Registration (check if email already exists)
     * 
     * Spring generates: SELECT * FROM users WHERE email = ?
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if email is already registered.
     * 
     * More efficient than findByEmail when you just need to check existence.
     * Spring generates: SELECT COUNT(*) > 0 FROM users WHERE email = ?
     */
    boolean existsByEmail(String email);
    
    /**
     * Find user by phone number.
     */
    Optional<User> findByPhoneNumber(String phoneNumber);
}
