package com.admin4tutor.server.service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.admin4tutor.server.model.entities.Lesson;

@Repository
public interface LessonRepository extends JpaRepository <Lesson, Long>{
    
}
