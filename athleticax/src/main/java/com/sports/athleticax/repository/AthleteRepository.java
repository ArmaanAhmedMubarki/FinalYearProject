package com.sports.athleticax.repository;

import com.sports.athleticax.entity.Athlete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AthleteRepository extends JpaRepository<Athlete, Long> {

    Optional<Athlete> findByUserId(Long userId);
}
