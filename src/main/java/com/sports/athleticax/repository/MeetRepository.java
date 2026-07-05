package com.sports.athleticax.repository;

import com.sports.athleticax.entity.Meet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MeetRepository extends JpaRepository<Meet, Long>
{
    Meet findByName(String name);
    Optional<Meet> findFirstByName(String name);
}


