package fr.insee.publicenemy.api.infrastructure.questionnaire;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.insee.publicenemy.api.infrastructure.questionnaire.entity.QuestionnaireEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface QuestionnaireEntityRepository extends JpaRepository<QuestionnaireEntity, Long> {
    @Query("select q from QuestionnaireEntity q join fetch q.modeEntities order by q.id desc")
    public List<QuestionnaireEntity> findAll();

    @Query("select q from QuestionnaireEntity q join fetch q.modeEntities where q.id=?1")
    public Optional<QuestionnaireEntity> findById(Long questionnaireId);
} 
