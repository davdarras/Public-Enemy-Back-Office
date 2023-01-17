package fr.insee.publicenemy.api.controllers.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import fr.insee.publicenemy.api.application.exceptions.ApiException;
import fr.insee.publicenemy.api.controllers.exceptions.dto.ApiError;


/**
 * Component used to build APIError objects 
 */
@Component
public class ApiExceptionComponent {

    /**
     * 
     * @param attributes attributes
     * @param request origin request
     * @param status status from exception
     * @param ex origin exception
     * @return error object used for JSON response
     */
    public ApiError buildErrorObject(Map<String, Object> attributes, WebRequest request, HttpStatus status,
            Exception ex) {
        return buildErrorObject(attributes, request, status, ex, null);
    }

    /**
     * 
     * @param attributes attributes
     * @param request origin request
     * @param status status from exception
     * @param ex origin exception
     * @param errorMessage error message
     * @return error object used for JSON response
     */
    public ApiError buildErrorObject(Map<String, Object> attributes, WebRequest request, HttpStatus status,
            Exception ex, String errorMessage) {

        ApiError errorObject = new ApiError();

        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        errorObject.setTimestamp((Date) attributes.get("timestamp"));
        errorObject.setPath(path);
        errorObject.setStatus(status.value());

        if (errorMessage == null || errorMessage.isEmpty()) {
            errorMessage = status.getReasonPhrase();
        }
        errorObject.addMessage(errorMessage);
        // Ajout de l'exception
        if (ex != null) {
            StringWriter stackTrace = new StringWriter();
            ex.printStackTrace(new PrintWriter(stackTrace));
            stackTrace.flush();

            errorObject.setStackTrace(stackTrace.toString());
            errorObject.setExceptionName(ex.getClass().getName());
            errorObject.setDebugMessage(ex.getLocalizedMessage());
        }

        return errorObject;
    }

    /**
     * 
     * @param attributes attributes
     * @param request origin request
     * @param ex origin exception
     * @return error object used for JSON response
     */
    public ApiError buildErrorObject(Map<String, Object> attributes, WebRequest request, ApiException ex) {
        ApiError errorObject = buildErrorObject(attributes, request, HttpStatus.valueOf(ex.getStatusCode()), ex);
        errorObject.setFieldErrors(ex.getErrors());
        errorObject.addMessage(ex.getMessage());
        return errorObject;
    }
}

