package com.admin4tutor.server.service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.admin4tutor.server.model.entities.Student;

@Repository
public interface StudentRepository extends JpaRepository <Student, Long> {

    @Query("SELECT s FROM Student s JOIN FETCH s.schedules WHERE s.telegramId = :telegram_id")
    Student findByTelegramIdWithSchedules(@Param("telegram_id")Long telegramId);
}
