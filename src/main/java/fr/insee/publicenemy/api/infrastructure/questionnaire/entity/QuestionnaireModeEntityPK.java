package fr.insee.publicenemy.api.infrastructure.questionnaire.entity;

import fr.insee.publicenemy.api.application.domain.model.Mode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite key for QuestionnaireModeEntity
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionnaireModeEntityPK implements Serializable {
    private QuestionnaireEntity questionnaire;
    private Mode mode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionnaireModeEntityPK that = (QuestionnaireModeEntityPK) o;
        return Objects.equals(questionnaire, that.questionnaire) && mode == that.mode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionnaire, mode);
    }
}
