package fr.insee.publicenemy.api.controllers.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import fr.insee.publicenemy.api.application.domain.model.Context;
import fr.insee.publicenemy.api.application.domain.model.Mode;
import fr.insee.publicenemy.api.application.domain.model.Questionnaire;
import lombok.NonNull;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record QuestionnaireRest(Long id, String poguesId, String label, Context context, List<Mode> modes){

    public static QuestionnaireRest createFromModel(@NonNull Questionnaire questionnaire) {
        return new QuestionnaireRest(questionnaire.id(), questionnaire.poguesId(), questionnaire.label(), questionnaire.context(), questionnaire.modes());
    }
}
