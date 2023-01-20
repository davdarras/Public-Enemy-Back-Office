package fr.insee.publicenemy.api.application.ports;

import fr.insee.publicenemy.api.application.domain.model.Ddi;
import fr.insee.publicenemy.api.application.domain.model.JsonLunatic;

public interface QueenServicePort {

    /**
     * Add questionnaire model in queen
     * @param questionnaireModelId questionnaire model id
     * @param ddi questionnaire DDI
     * @param jsonLunatic json lunatic for this questionnaire model
     * @return
     */
    void createQuestionnaireModel(String questionnaireModelId, Ddi ddi, JsonLunatic jsonLunatic);

    /**
     * Create campaign in queen
     * @param campaignId campaign id
     * @param ddi questionnaire DDI
     */
    void createCampaign(String campaignId, Ddi ddi);

    /**
     * Delete campaign in queen
     * @param campaignId campaign id
     */
    void deleteCampaign(String campaignId);
}