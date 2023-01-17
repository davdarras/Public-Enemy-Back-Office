package fr.insee.publicenemy.api.controllers.dto;

import fr.insee.publicenemy.api.application.domain.model.Context;
public record QuestionnaireAddRest(String poguesId, Context context) {
}
