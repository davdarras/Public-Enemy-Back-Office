package fr.insee.publicenemy.api.application.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insee.publicenemy.api.application.domain.model.Context;
import fr.insee.publicenemy.api.application.domain.model.Ddi;
import fr.insee.publicenemy.api.application.domain.model.JsonLunatic;
import fr.insee.publicenemy.api.application.domain.model.Mode;
import fr.insee.publicenemy.api.application.domain.model.Questionnaire;
import fr.insee.publicenemy.api.application.ports.DdiServicePort;
import fr.insee.publicenemy.api.application.ports.EnoServicePort;

@Service
@Transactional
public class DDIUseCase {
    
    private final DdiServicePort ddiService;
    private final EnoServicePort enoService;

    public DDIUseCase(DdiServicePort ddiService, EnoServicePort enoService) {
        this.ddiService = ddiService;
        this.enoService = enoService;
    }

    /**
     * Get questionnaire
     * @param poguesId pogues questionnaire id
     * @return the questionnaire
     */
    public Questionnaire getQuestionnaire(String poguesId) {
        return ddiService.getQuestionnaire(poguesId);
    }

    /**
     * Get DDI as XML format from questionnaire Id
     * @param poguesId pogues questionnaire id
     * @return DDI
     */
    public Ddi getDdi(String poguesId) {
        return ddiService.getDdi(poguesId);
    }

    /**
     * Convert DDI with given identifier to a Lunatic questionnaire (json format)
     * @param ddi Ddi
     * @param context insee context
     * @param mode questionnaire mode
     * @return Json Lunatic
     */
    public JsonLunatic getJsonLunatic(Ddi ddi, Context context, Mode mode) {
        return enoService.getJsonLunatic(ddi, context, mode);
    }
}
