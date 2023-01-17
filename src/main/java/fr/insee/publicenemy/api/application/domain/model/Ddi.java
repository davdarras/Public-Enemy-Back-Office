package fr.insee.publicenemy.api.application.domain.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public record Ddi(String poguesId, String label, List<Mode> modes, byte[] content) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ddi ddi = (Ddi) o;
        return Objects.equals(poguesId, ddi.poguesId) && Objects.equals(label, ddi.label) && Objects.equals(modes, ddi.modes) && Arrays.equals(content, ddi.content);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(poguesId, label, modes);
        result = 31 * result + Arrays.hashCode(content);
        return result;
    }

    @Override
    public String toString() {
        return "Ddi{" +
                "poguesId='" + poguesId + '\'' +
                ", label='" + label + '\'' +
                ", modes=" + modes +
                ", content=" + Arrays.toString(content) +
                '}';
    }
}
