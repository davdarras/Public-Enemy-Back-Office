package fr.insee.publicenemy.api.application.domain.model;

/**
 * State used for synchronisation with queen
 */
public enum SynchronisationState {
    INIT_QUESTIONNAIRE,
    INIT_CAMPAIGN,
    INIT_SURVEY_UNIT,
    OK;
}
