package fr.insee.publicenemy.api.application.domain.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import jakarta.validation.constraints.NotNull;

public record Questionnaire(
    Long id,
    @NotNull String poguesId,
    @NotNull String label,
    @NotNull Context context,
    @NotNull List<Mode> modes,
    @NotNull byte[] surveyUnitData,
    Date updatedDate) {

    public Questionnaire(String poguesId, String label, Context context, List<Mode> modes, byte[] surveyUnitData) {
        this(null, poguesId, label, context, modes, surveyUnitData, null);
    }

    public Questionnaire(Long id, Context context, byte[] surveyUnitData) {
        this(id, null, null, context, null, surveyUnitData, null);
    }

    public Questionnaire(String poguesId, String label, List<Mode> modes) {
        this(null, poguesId, label, null, modes, null, null);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Questionnaire that = (Questionnaire) o;
        return Objects.equals(id, that.id) && Objects.equals(poguesId, that.poguesId)
                && Objects.equals(label, that.label) && context == that.context && Objects.equals(modes, that.modes)
                && Arrays.equals(surveyUnitData, that.surveyUnitData) && Objects.equals(updatedDate, that.updatedDate);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, poguesId, label, context, modes, updatedDate);
        result = 31 * result + Arrays.hashCode(surveyUnitData);
        return result;
    }

    @Override
    public String toString() {
        return "Questionnaire{" +
                "id=" + id +
                ", poguesId='" + poguesId + '\'' +
                ", label='" + label + '\'' +
                ", context=" + context +
                ", modes=" + modes +
                ", surveyUnitData=" + new String(surveyUnitData) +
                ", updatedDate=" + updatedDate +
                '}';
    }
}
