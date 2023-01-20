package fr.insee.publicenemy.api.infrastructure.questionnaire.entity;

import fr.insee.publicenemy.api.application.domain.model.Mode;
import fr.insee.publicenemy.api.application.domain.model.QuestionnaireMode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "questionnaire_mode")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(QuestionnaireModeEntityPK.class)
public class QuestionnaireModeEntity implements Serializable {
    @Id
    @Column(name="mode")
    @Enumerated(EnumType.STRING)
    private Mode mode;

    @Id
    @ManyToOne
    @JoinColumn(name = "questionnaire_id")
    private QuestionnaireEntity questionnaire;

    @Column(name="state")
    private String synchronisationState;

    /**
     * Constructorer
     * @param questionnaire the questionnaire linked to this questionnaire mode
     * @param mode insee mode
     * @param synchronisationState synchronisation state of this mode in orchestrators
     */
    public QuestionnaireModeEntity(QuestionnaireEntity questionnaire, Mode mode, String synchronisationState) {
        this.mode = mode;
        this.questionnaire = questionnaire;
        this.synchronisationState = synchronisationState;
    }

    /**
     * @param modeEntity mode entity
     * @return the application model of this entity
     */
    public static QuestionnaireMode toModel(QuestionnaireModeEntity modeEntity) {
        return new QuestionnaireMode(modeEntity.getQuestionnaire().getId(),
                modeEntity.getMode(),
                modeEntity.getSynchronisationState());
    }

    /**
     * @param modeEntities mode entities
     * @return the application model of these entities
     */
    public static List<QuestionnaireMode> toModel(List<QuestionnaireModeEntity> modeEntities) {
        return modeEntities.stream().map(QuestionnaireModeEntity::toModel).toList();
    }

    /**
     * Permits to create a mode entity before saving it to persistence unit
     * @param questionnaireEntity the questionnaire entity to link to the mode entity
     * @param questionnaireMode application mode model
     * @return
     */
    public static QuestionnaireModeEntity createEntity(QuestionnaireEntity questionnaireEntity, QuestionnaireMode questionnaireMode) {
        return new QuestionnaireModeEntity(questionnaireEntity, questionnaireMode.getMode(), questionnaireMode.getSynchronisationState());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionnaireModeEntity that = (QuestionnaireModeEntity) o;
        return mode == that.mode && Objects.equals(questionnaire, that.questionnaire) && Objects.equals(synchronisationState, that.synchronisationState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mode, questionnaire, synchronisationState);
    }

    @Override
    public String toString() {
        return "QuestionnaireModeEntity{" +
                "mode=" + mode +
                ", questionnaire=" + questionnaire.getId() +
                ", synchronisationState='" + synchronisationState + '\'' +
                '}';
    }
}