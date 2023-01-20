package fr.insee.publicenemy.api.application.usecase;

import java.util.List;

import fr.insee.publicenemy.api.application.domain.model.*;
import fr.insee.publicenemy.api.application.exceptions.ServiceException;
import fr.insee.publicenemy.api.application.ports.QuestionnairePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class QuestionnaireUseCase {
    private final QuestionnairePort questionnairePort;

    private final QueenSynchronizationUseCase queenUseCase;

    private final DDIUseCase ddiUseCase;

    public QuestionnaireUseCase(QuestionnairePort questionnairePort, DDIUseCase ddiUseCase, QueenSynchronizationUseCase queenUseCase) {
        this.questionnairePort = questionnairePort;
        this.ddiUseCase = ddiUseCase;
        this.queenUseCase = queenUseCase;
    }

    /**
     * Add questionnaire
     * @param poguesId pogues questionnaire id
     * @param context insee context
     * @param csvContent survey unit data file (csv)
     * @return the saved questionnaire
     */
    public Questionnaire addQuestionnaire(String poguesId, Context context, byte[] csvContent) {
        Ddi ddi = ddiUseCase.getDdi(poguesId);

        Questionnaire questionnaire = new Questionnaire(ddi, context, csvContent);
        questionnaire = questionnairePort.addQuestionnaire(questionnaire);
        try {
            queenUseCase.synchronizeCreate(ddi, context, questionnaire);
        } catch(ServiceException ex) {
            // fail silently as the questionnaire is already created
            log.error(ex.toString());
        }

        // update questionnaire to save the synchronisation state (unsuccessful in case of throwed ServiceException)
        questionnairePort.updateQuestionnaireState(questionnaire);
        return questionnaire;
    }

    /**
     * Get questionnaire
     * @param id questionnaire id
     * @return the questionnaire
     */
    public Questionnaire getQuestionnaire(Long id) {
        return questionnairePort.getQuestionnaire(id);
    }

    /**
     * Get questionnaire list
     * @return the questionnaire list
     */
    public List<Questionnaire> getQuestionnaires() {
        return questionnairePort.getQuestionnaires();
    }

    /**
     * delete questionnaire
     * @param id questionnaire id
     */
    public void deleteQuestionnaire(Long id) {
        Questionnaire questionnaire = questionnairePort.getQuestionnaire(id);
        questionnairePort.deleteQuestionnaire(id);
        queenUseCase.synchronizeDelete(questionnaire);
    }

    /**
     * Update questionnaire
     * @param id questionnaire id
     * @param context insee context
     * @param surveyUnitData survey unit data file in csv format
     * @return the saved questionnaire
     */
    public Questionnaire updateQuestionnaire(Long id, Context context, byte[] surveyUnitData) {
        Questionnaire questionnaire = new Questionnaire(id, context, surveyUnitData);
        return questionnairePort.updateQuestionnaire(questionnaire);
    }
}
