package fr.insee.publicenemy.api.controllers.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import fr.insee.publicenemy.api.application.domain.model.Context;
import fr.insee.publicenemy.api.application.domain.model.Mode;
import fr.insee.publicenemy.api.application.domain.model.Questionnaire;
import fr.insee.publicenemy.api.application.domain.model.QuestionnaireMode;
import lombok.NonNull;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record QuestionnaireRest(Long id, String poguesId, String label, Context context, List<Mode> modes, boolean isSynchronized){

    public static QuestionnaireRest createFromModel(@NonNull Questionnaire questionnaire) {
        List<Mode> modes = questionnaire.getQuestionnaireModes().stream().map(QuestionnaireMode::getMode).toList();
        return new QuestionnaireRest(questionnaire.getId(), questionnaire.getPoguesId(), questionnaire.getLabel(),
                questionnaire.getContext(), modes, questionnaire.isSynchronized());
    }
}
