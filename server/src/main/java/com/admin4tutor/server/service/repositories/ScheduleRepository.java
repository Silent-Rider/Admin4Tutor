package com.admin4tutor.server.service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.admin4tutor.server.model.entities.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

}
