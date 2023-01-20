package fr.insee.publicenemy.api.infrastructure.queen;

import fr.insee.publicenemy.api.application.domain.model.*;
import fr.insee.publicenemy.api.application.exceptions.ServiceException;
import fr.insee.publicenemy.api.application.ports.QueenServicePort;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;

@Service
@Slf4j
public class QueenServiceImpl implements QueenServicePort {

    private final WebClient webClient;
    private final String queenUrl;

    public QueenServiceImpl(WebClient webClient, @Value("${application.queen.url}") String queenUrl) {
        this.webClient = webClient; 
        this.queenUrl = queenUrl;
    }

    public void createQuestionnaireModel(String questionnaireModelId, @NotNull Ddi ddi, @NotNull JsonLunatic jsonLunatic) {
        QuestionnaireModelDto questionnaireModel = new QuestionnaireModelDto(questionnaireModelId, ddi.label(), new ArrayList<>(), jsonLunatic.jsonContent());

        URI uri = UriComponentsBuilder
                .fromHttpUrl(queenUrl)
                .path("/api/questionnaire-models")
                .build()
                .toUri();

        webClient.post().uri(uri)
                .body(BodyInserters.fromValue(questionnaireModel))
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorMessage -> Mono.error(new ServiceException(response.statusCode().value(), errorMessage)))
                )
                .toBodilessEntity()
                .block();
    }

    public void createCampaign(@NotNull String campaignId, Ddi ddi) {
        CampaignDto campaign = new CampaignDto(campaignId, ddi.label());

        URI uri = UriComponentsBuilder
                .fromHttpUrl(queenUrl)
                .path("/api/campaigns")
                .build()
                .toUri();

        webClient.post().uri(uri)
                .body(BodyInserters.fromValue(campaign))
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> Mono.error(new ServiceException(response.statusCode().value(),
                               String.format("Error trying to create queen campaign %s", campaignId)))
                )
                .toBodilessEntity()
                .block();
    }

    public void deleteCampaign(String campaignId) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(queenUrl)
                .path("/api/campaign/{id}")
                .queryParam("force", true)
                .build(campaignId);

        webClient.delete()
                .uri(uri)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> Mono.error(new ServiceException(response.statusCode().value(),
                                        String.format("Error trying to delete queen campaign %s", campaignId)))
                )
                .toBodilessEntity()
                .block();
    }
}
