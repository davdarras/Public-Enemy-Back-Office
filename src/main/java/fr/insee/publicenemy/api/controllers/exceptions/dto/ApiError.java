package fr.insee.publicenemy.api.controllers.exceptions.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Error object returned as JSON response to client
 */
public record ApiError(
    Integer status,
    String path,
    List<String> messages,
    @JsonIgnore
    String debugMessage,
    @JsonIgnore
    String exceptionName,
    @JsonInclude(Include.NON_NULL)
    List<ApiFieldError> fieldErrors,
    @JsonIgnore
    String stackTrace,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
    Date timestamp) {

    public ApiError(int status, String path, String errorMessage, Date timestamp) {
        this(status, path, List.of(errorMessage), null, null,  new ArrayList<>(), null, timestamp);
    }

    public ApiError(int status, String path, String errorMessage, String stacktrace, String exceptionName, String debugMessage, Date timestamp) {
        this(status, path, List.of(errorMessage), debugMessage, exceptionName, new ArrayList<>(), stacktrace, timestamp);
    }

    public void addFieldErrors(List<ApiFieldError> fieldErrors) {
        this.fieldErrors.addAll(fieldErrors);
    }
}