package fr.insee.publicenemy.api.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.insee.publicenemy.api.application.domain.model.Context;
import fr.insee.publicenemy.api.application.domain.model.Ddi;
import fr.insee.publicenemy.api.application.domain.model.JsonLunatic;
import fr.insee.publicenemy.api.application.domain.model.Mode;
import fr.insee.publicenemy.api.application.usecase.DDIUseCase;

@RestController
@RequestMapping("/api/ddi")
public class DdiController {

    DDIUseCase ddiUseCase;

    public DdiController(DDIUseCase ddiUseCase) {
        this.ddiUseCase = ddiUseCase;
    }

    @GetMapping("/{poguesId}")
    public Ddi getDdi(@PathVariable String poguesId) {
        return ddiUseCase.getDdi(poguesId);
    }

    @GetMapping("/json-lunatic/{poguesId}")
    public JsonLunatic getJsonLunatic(@PathVariable String poguesId) {
        return ddiUseCase.getJsonLunatic(poguesId, Context.HOUSEHOLD, Mode.CAWI);
    }
}
