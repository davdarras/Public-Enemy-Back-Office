package fr.insee.publicenemy.api.infrastructure.queen;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record CampaignDto(
        @JsonProperty("id")
        String id,
        @JsonProperty("label")
        String label,
        @JsonProperty("questionnaireIds")
        List<String> questionnaireIds) {

        public CampaignDto(String id, String label) {
                this(id, label, List.of(id));
        }
}
