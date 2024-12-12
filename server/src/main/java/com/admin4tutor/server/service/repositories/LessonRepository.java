package com.admin4tutor.server.service.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.admin4tutor.server.model.Status;
import com.admin4tutor.server.model.entities.Lesson;
import com.admin4tutor.server.model.entities.Student;


@Repository
public interface LessonRepository extends JpaRepository <Lesson, Long>{
    
    List<Lesson> findByStudentAndStatus(Student student, Status status);
}
