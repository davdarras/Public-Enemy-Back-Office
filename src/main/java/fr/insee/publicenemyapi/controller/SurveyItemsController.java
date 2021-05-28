package fr.insee.publicenemyapi.controller;

import fr.insee.publicenemyapi.services.SurveyItemsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.MediaType;

@RestController
public class SurveyItemsController {

    @Autowired
    SurveyItemsServices qs;

    @RequestMapping(value = "/survey/{id}/metadata", produces= "application/json", consumes = MediaType.MULTIPART_FORM_DATA, method = { RequestMethod.GET })


}
