package com.sports.athleticax.repository;

import com.sports.athleticax.entity.Registration;
import com.sports.athleticax.entity.User;
import com.sports.athleticax.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    // Find by eventId and athleteId
    Optional<Registration> findByEventIdAndAthleteId(Long eventId, Long athleteId);


    List<Registration> findByAthleteId(Long athleteId); // Find by athleteId

    List<Registration> findByEventId(Long eventId); // Find by eventId
}

