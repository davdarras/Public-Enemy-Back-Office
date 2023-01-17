package fr.insee.publicenemy.api.application.domain.model;

import java.util.Arrays;


public record JsonLunatic(byte[] jsonContent) {
    @Override
    public String toString() {
        return "JsonLunatic{" +
                "jsonContent=" + Arrays.toString(jsonContent) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonLunatic that = (JsonLunatic) o;
        return Arrays.equals(jsonContent, that.jsonContent);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(jsonContent);
    }
}
