//package com.sports.athleticax.services;
//
//import com.sports.athleticax.repository.MeetRepository;
//import com.sports.athleticax.dto.MeetDTO;
//import com.sports.athleticax.entity.Meet;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class MeetService {
//
//    @Autowired
//    private MeetRepository meetRepository;
//
//    public List<Meet> getMeet(){
//
//        List<Meet> meetDetails = meetRepository.findAll();
//
//        if (meetDetails.isEmpty()) {
//            throw new RuntimeException("No meet available");
//        }
//
//        return meetDetails;
//    }
//
//    public void setMeet(Meet meet){
//
//        if (meet.getName() == null){
//            throw new RuntimeException("A meet name is must required");}
//
//        meetRepository.save(meet);
//    }
//
//    public void deleteMeet(Long id) {
//        meetRepository.delete(meetRepository.getReferenceById(id));
//    }
//}
//
