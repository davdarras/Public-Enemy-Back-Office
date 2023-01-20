package fr.insee.publicenemy.api.controllers;

import java.io.IOException;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import fr.insee.publicenemy.api.application.domain.model.Context;
import fr.insee.publicenemy.api.application.domain.model.Questionnaire;
import fr.insee.publicenemy.api.application.usecase.DDIUseCase;
import fr.insee.publicenemy.api.application.usecase.QuestionnaireUseCase;
import fr.insee.publicenemy.api.controllers.dto.QuestionnaireAddRest;
import fr.insee.publicenemy.api.controllers.dto.QuestionnaireRest;

@RestController
@RequestMapping("/api/questionnaires")
@Slf4j
public class QuestionnaireController {

    private final QuestionnaireUseCase questionnaireUseCase;
    private final DDIUseCase ddiUseCase;

    public QuestionnaireController(QuestionnaireUseCase questionnaireUseCase, DDIUseCase ddiUseCase) {
        this.questionnaireUseCase = questionnaireUseCase;
        this.ddiUseCase = ddiUseCase;
    }

    /**
     * 
     * @return all questionnaires
     */
    @GetMapping("")
    public List<QuestionnaireRest> getQuestionnaires() {
        return questionnaireUseCase.getQuestionnaires().stream()
                .map(QuestionnaireRest::createFromModel)
                .toList();      
    }

    /**
     * 
     * @param id questionnaire id
     * @return questionnaire
     */
    @GetMapping("/{id}")
    public QuestionnaireRest getQuestionnaire(@PathVariable Long id) {        
        Questionnaire questionnaire = questionnaireUseCase.getQuestionnaire(id);
        return QuestionnaireRest.createFromModel(questionnaire);
    }

    /**
     * 
     * @param poguesId pogues questionnaire id
     * @return questionnaire informations from ddi
     */
    @GetMapping("/pogues/{poguesId}")
    public QuestionnaireRest getQuestionnaireFromPogues(@PathVariable String poguesId) {        
        Questionnaire questionnaire = ddiUseCase.getQuestionnaire(poguesId);
        return QuestionnaireRest.createFromModel(questionnaire);
    }

    /**
     * 
     * @param questionnaireRest questionnaire form
     * @param surveyUnitData csv content of survey units
     * @return the saved questionnaire
     */
    @PostMapping(path = "/add", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public QuestionnaireRest addQuestionnaire(
            @RequestPart(name = "questionnaire") QuestionnaireAddRest questionnaireRest,
            @RequestPart(name = "surveyUnitData") MultipartFile surveyUnitData) throws IOException {
        
        byte[] csvContent = surveyUnitData.getBytes();
        Questionnaire questionnaire = questionnaireUseCase.addQuestionnaire(questionnaireRest.poguesId(), questionnaireRest.context(), csvContent);
        return QuestionnaireRest.createFromModel(questionnaire);
    }

    /**
     * 
     * @param id questionnaire id
     * @param context insee context
     * @param surveyUnitData csv content of survey units
     * @return the updated questionnaire
     */
    @PostMapping(path = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public QuestionnaireRest saveQuestionnaire(
            @PathVariable Long id,
            @RequestPart(name = "context") Context context,
            @RequestPart(name = "surveyUnitData", required = false) MultipartFile surveyUnitData) throws IOException {        
        
        byte[] csvContent = null;
        if(surveyUnitData != null) {
            csvContent = surveyUnitData.getBytes(); 
        }
        Questionnaire questionnaire = questionnaireUseCase.updateQuestionnaire(id, context, csvContent);
        return QuestionnaireRest.createFromModel(questionnaire);
    }

    /**
     * Delete questionnaire
     * @param id  questionnaire id to delete
     */
    @DeleteMapping(path = "/{id}/delete")
    public String deleteQuestionnaire(
            @PathVariable Long id) {        
        questionnaireUseCase.deleteQuestionnaire(id);
        return "{}";
    }
}
