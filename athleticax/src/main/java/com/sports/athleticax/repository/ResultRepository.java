//package com.sports.athleticax.repository;
//
//import com.sports.athleticax.entity.Result;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface ResultRepository extends JpaRepository<Result, Long> {
//    // Find by eventId and athleteId
//    Optional<Result> findByAthleteIdAndEventId(Long athleteId, Long eventId);
//
//    List<Result> findByAthleteId(Long athleteId); // Find by athleteId
//
//    List<Result> findByEventId(Long eventId); // Find by eventId
//}