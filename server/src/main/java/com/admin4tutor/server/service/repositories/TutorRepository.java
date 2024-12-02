package com.admin4tutor.server.service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.admin4tutor.server.model.entities.Tutor;
import java.util.List;
import com.admin4tutor.server.model.Language;


@Repository
public interface TutorRepository extends JpaRepository <Tutor, Long> {
    
    List<Tutor> findByLanguage(Language language);
}
