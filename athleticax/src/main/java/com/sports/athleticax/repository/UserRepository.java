package com.sports.athleticax.repository;

import com.sports.athleticax.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);// New method for finding by email
    @Query("SELECT COUNT(u) > 0 FROM User u JOIN u.roles r WHERE r.name = 'ADMIN'")
    boolean existsAdmin(); // Used to check if an admin already exists
}
