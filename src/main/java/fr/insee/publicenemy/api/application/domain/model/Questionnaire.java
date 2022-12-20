package fr.insee.publicenemy.api.application.domain.model;

import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Questionnaire {

    private Long id;

    @NotNull
    private String poguesId;

    @NotNull
    private String label;

    @NotNull
    private Context context;

    @NotNull
    private List<Mode> modes;

    @NotNull
    private byte[] surveyUnitData;

    private Date updatedDate;

    public Questionnaire(String poguesId, String label, Context context, List<Mode> modes, byte[] surveyUnitData) {
        this.poguesId = poguesId;
        this.label = label;
        this.context = context;
        this.modes = modes;
        this.surveyUnitData = surveyUnitData;
    }

    public Questionnaire(Long id, Context context, byte[] surveyUnitData) {
        this.id = id;
        this.context = context;
        this.surveyUnitData = surveyUnitData;
    }

    public Questionnaire(String poguesId, String label, List<Mode> modes) {
        this.poguesId = poguesId;
        this.label = label;
        this.modes = modes;
    }
}
