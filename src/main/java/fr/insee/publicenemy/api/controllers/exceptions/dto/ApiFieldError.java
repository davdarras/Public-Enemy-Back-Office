package fr.insee.publicenemy.api.controllers.exceptions.dto;

import java.io.Serializable;

/**
 * Subfield Error object returned as JSON response to client
 */
public record ApiFieldError(String field, String message) implements Serializable {
}
