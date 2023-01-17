package fr.insee.publicenemy.api.infrastructure.ddi.exceptions;

public class DdiNotFoundException extends RuntimeException {
    public DdiNotFoundException(String poguesId){
        super(String.format("XML DDI is empty for pogues ID: %s", poguesId));
    }
}
