package fr.insee.publicenemy.api.infrastructure.questionnaire;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.insee.publicenemy.api.infrastructure.questionnaire.entity.QuestionnaireEntitySummary;

public interface QuestionnaireEntitySummaryRepository  extends JpaRepository<QuestionnaireEntitySummary, Long> {
    
    @Query("SELECT q FROM QuestionnaireEntitySummary q join fetch q.modes")
    List<QuestionnaireEntitySummary> findAll();
} 
    
