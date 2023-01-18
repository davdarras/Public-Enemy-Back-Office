package fr.insee.publicenemy.api.infrastructure.questionnaire.entity;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fr.insee.publicenemy.api.application.domain.model.Context;
import fr.insee.publicenemy.api.application.domain.model.Mode;
import fr.insee.publicenemy.api.application.domain.model.Questionnaire;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="questionnaire")
public class QuestionnaireEntity extends BaseQuestionnaireEntity {

    @Lob
    @Column
    @NotNull
    private byte[] surveyUnitData;

    public QuestionnaireEntity(String questionnaireId, String label, Context context, List<Mode> modes, byte[] surveyUnitData) {
        super(questionnaireId, label, context, modes);
        this.surveyUnitData = surveyUnitData;
    }

    public static QuestionnaireEntity createFromModel(@NonNull Questionnaire questionnaire) {
        QuestionnaireEntity questionnaireEntity = new QuestionnaireEntity(questionnaire.getPoguesId(), questionnaire.getLabel(),questionnaire.getContext(), questionnaire.getModes(), questionnaire.getSurveyUnitData());
        Date date = Calendar.getInstance().getTime();
    
        questionnaireEntity.setCreationDate(date);
        questionnaireEntity.setUpdatedDate(date);
        return questionnaireEntity;
    }

    /**
     * Update questionnaire entity from questionnaire
     * @param questionnaire questionnaire to update
     */
    public void update(@NonNull Questionnaire questionnaire) {
        byte [] questionnaireUnitData = questionnaire.surveyUnitData();
        if(questionnaireUnitData != null && questionnaireUnitData.length > 0) {
            setSurveyUnitData(questionnaireUnitData);
        }
        setContext(questionnaire.context());
        setUpdatedDate(Calendar.getInstance().getTime());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Arrays.hashCode(surveyUnitData);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        QuestionnaireEntity other = (QuestionnaireEntity) obj;
        return Arrays.equals(surveyUnitData, other.surveyUnitData);
    }

    
}
