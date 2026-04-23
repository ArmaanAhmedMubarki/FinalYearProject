//package com.sports.athleticax.repository;
//
//import com.sports.athleticax.entity.Assistance;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface AssistanceRepository extends JpaRepository<Assistance, Long> {
//
//    // Find by coachId and athleteId
//    Optional<Assistance> findByCoachIdAndAthleteId(Long coachId, Long athleteId);
//
//
//    List<Assistance> findByAthleteId(Long athleteId); // Find by athleteId
//
//    List<Assistance> findByCoachId(Long coachId); // Find by coachId
//}
//
