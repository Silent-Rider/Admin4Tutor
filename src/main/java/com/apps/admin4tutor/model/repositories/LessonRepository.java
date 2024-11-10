package com.apps.admin4tutor.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.apps.admin4tutor.model.entities.Lesson;

public interface LessonRepository extends JpaRepository <Lesson, Long>{
    
}
