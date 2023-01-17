package fr.insee.publicenemy.api.infrastructure.ddi.exceptions;

public class PoguesJsonNotFoundException extends RuntimeException {
    public PoguesJsonNotFoundException(String poguesId){
        super(String.format("JSON Pogues is empty for pogues id: %s", poguesId));
    }
}
