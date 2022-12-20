package fr.insee.publicenemy.api.infrastructure.ddi;

import java.util.List;

import fr.insee.publicenemy.api.application.domain.model.Mode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PoguesDataSummary {
    private String label;
    private List<Mode> modes;
}
