//package com.sports.athleticax.services;
//
//import com.sports.athleticax.repository.AssistanceRepository;
//import com.sports.athleticax.repository.AthleteRepository;
//import com.sports.athleticax.repository.CoachRepository;
//import com.sports.athleticax.dto.AssistanceDTO;
//import com.sports.athleticax.entity.Assistance;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class AssistanceService {
//
//    @Autowired
//    private AssistanceRepository assistanceRepository;
//
//    @Autowired
//    private AthleteRepository athleteRepository;
//
//    @Autowired
//    private CoachRepository coachRepository;
//
//    public Optional<Assistance> findByCoachIdAndAthleteId(Long coachId, Long athleteId) {
//        return assistanceRepository.findByCoachIdAndAthleteId(coachId, athleteId);
//    }
//
//    public List<Assistance> findByCoachId(Long coachId) {
//        return assistanceRepository.findByCoachId(coachId);
//    }
//
//    public List<Assistance> findByAthleteId(Long athleteId) {
//        return assistanceRepository.findByAthleteId(athleteId);
//    }
//
//    public void createAssistance(AssistanceDTO assistanceDTOObject) {
//        Assistance assistance = new Assistance();
//        assistance.setAthleteId(assistanceDTOObject.getAthleteId());
//        assistance.setCoachId(assistanceDTOObject.getCoachId());
//        assistance.setRequestDate(assistanceDTOObject.getRequestDate());
//        assistance.setStatus("PENDING");
//
//        assistanceRepository.save(assistance);
//    }
//
//    public void updateAssistance(Long id, String status, String remarks) {
//        Assistance assistance = assistanceRepository.getReferenceById(id);
//        assistance.setStatus(status);
//        assistance.setRemarks(remarks);
//        assistanceRepository.save(assistance);
//    }
//
//    public void deleteAssistance(Long id) {
//        Assistance assistance = assistanceRepository.getReferenceById(id);
//        assistanceRepository.delete(assistance);
//    }
//}
//
