package fr.insee.publicenemy.api.infrastructure.ddi;

import java.util.List;

import fr.insee.publicenemy.api.application.domain.model.Mode;


public record PoguesDataSummary(String label, List<Mode> modes) {
}
