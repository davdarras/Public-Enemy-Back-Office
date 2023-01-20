package fr.insee.publicenemy.api.application.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@AllArgsConstructor
public class QuestionnaireMode {

    private Long id;
    private Mode mode;
    private String synchronisationState;

    public QuestionnaireMode(Mode mode) {
        this(null, mode, null);
    }

    public QuestionnaireMode(Mode mode, String synchronisationState) {
        this(null, mode, synchronisationState);
    }

    public static List<QuestionnaireMode> toModel(List<Mode> modes) {
        return modes.stream().map(QuestionnaireMode::new).toList();
    }

    public void setSynchronisationState(String synchronisationState) {
        this.synchronisationState = synchronisationState;
    }
}
