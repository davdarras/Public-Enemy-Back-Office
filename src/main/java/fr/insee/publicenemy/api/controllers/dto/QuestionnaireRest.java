package fr.insee.publicenemy.api.controllers.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import fr.insee.publicenemy.api.application.domain.model.Context;
import fr.insee.publicenemy.api.application.domain.model.Mode;
import fr.insee.publicenemy.api.application.domain.model.Questionnaire;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class QuestionnaireRest {

    private Long id;

    private String poguesId;

    private String label;

    private Context context;

    private List<Mode> modes;

    public static QuestionnaireRest createFromModel(Questionnaire questionnaire) {
        QuestionnaireRest questionnaireRest = new QuestionnaireRest();
        questionnaireRest.setId(questionnaire.getId());
        questionnaireRest.setLabel(questionnaire.getLabel());
        questionnaireRest.setPoguesId(questionnaire.getPoguesId());
        questionnaireRest.setModes(questionnaire.getModes());
        questionnaireRest.setContext(questionnaire.getContext());    
        
        return questionnaireRest;
    }
}
