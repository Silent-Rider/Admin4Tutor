package com.admin4tutor.server.service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.admin4tutor.server.model.entities.Tutor;
import java.util.List;
import com.admin4tutor.server.model.Language;


@Repository
public interface TutorRepository extends JpaRepository <Tutor, Long> {
    
    @Query("SELECT t FROM Tutor t JOIN FETCH t.availabilities WHERE t.language = :language")
    List<Tutor> findByLanguageWithAvailabilities(@Param("language") Language language);
}
