package fr.insee.publicenemy.api.controllers.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.insee.publicenemy.api.application.exceptions.ServiceException;
import fr.insee.publicenemy.api.infrastructure.ddi.exceptions.LunaticJsonNotFoundException;
import fr.insee.publicenemy.api.infrastructure.ddi.exceptions.PoguesJsonNotFoundException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import fr.insee.publicenemy.api.application.exceptions.ApiException;
import fr.insee.publicenemy.api.controllers.exceptions.dto.ApiError;
import fr.insee.publicenemy.api.controllers.exceptions.dto.ApiFieldError;
import fr.insee.publicenemy.api.infrastructure.i18n.I18nMessageServiceImpl;
import fr.insee.publicenemy.api.infrastructure.questionnaire.RepositoryEntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

/**
 * Handle API exceptions for project
 * Do not work on exceptions occuring before/outside controllers scope
 */
@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    private final ApiExceptionComponent errorComponent;

    private final ErrorAttributes errorAttributes;

    private final I18nMessageServiceImpl messageService;

    public ApiExceptionHandler(ApiExceptionComponent errorComponent, ErrorAttributes errorAttributes, I18nMessageServiceImpl messageService) {
        this.errorComponent = errorComponent;
        this.errorAttributes = errorAttributes;
        this.messageService = messageService;
    }

    private static final String INTERNAL_EXCEPTION_KEY = "exception.internal";
    private static final String VALIDATION_EXCEPTION_KEY = "exception.validation";
    private static final String NOTFOUND_EXCEPTION_KEY = "exception.notfound";
    private static final String EXCEPTION_OCCURRED_KEY = "exception.occurred";

    /** Global method to process the catched exception
     * @param ex Exception catched
     * @param statusCode status code linked with this exception
     * @param request request initiating the exception
     * @return the apierror object with associated status code
     */
    private ResponseEntity<ApiError> processException(Exception ex, int statusCode, WebRequest request) {
        return processException(ex, HttpStatus.valueOf(statusCode), request);
    }

    /** Global method to process the catched exception
     * @param ex Exception catched
     * @param status status linked with this exception
     * @param request request initiating the exception
     * @return the apierror object with associated status code
     */
    private ResponseEntity<ApiError> processException(Exception ex, HttpStatus status, WebRequest request) {
        log.error(messageService.getMessage(EXCEPTION_OCCURRED_KEY), ex);
        Map<String, Object> attributes = errorAttributes.getErrorAttributes(request, ErrorAttributeOptions.defaults());
        ApiError error = errorComponent.buildErrorObject(attributes, request, status, ex, ex.getMessage());
        return new ResponseEntity<>(error, status);
    }

    /** Global method to process the catched exception
     * @param ex Exception catched
     * @param status status linked with this exception
     * @param request request initiating the exception
     * @param overrideErrorMessage message overriding default error message from exception
     * @return the apierror object with associated status code
     */
    private ResponseEntity<ApiError> processException(Exception ex, HttpStatus status, WebRequest request, String overrideErrorMessage) {
        log.error(messageService.getMessage(EXCEPTION_OCCURRED_KEY), ex);
        Map<String, Object> attributes = errorAttributes.getErrorAttributes(request, ErrorAttributeOptions.defaults());
        String errorMessage = ex.getMessage();
        if(overrideErrorMessage != null) {
            errorMessage = overrideErrorMessage;
        }
        ApiError error = errorComponent.buildErrorObject(attributes, request, status, ex, errorMessage);
        return new ResponseEntity<>(error, status);
    }

    /**
     * Handle Service Exceptions
     *
     * @param ex      DdiException
     * @param request WebRequest object WebRequest
     * @return the ApiError object
     */
    @ExceptionHandler({ ServiceException.class })
    public ResponseEntity<ApiError> handleServiceException(
            ServiceException ex,
            WebRequest request) {
        return processException(ex, ex.getCode(), request);
    }

    /**
     * Handle JSON pogues empty exception
     *
     * @param ex      PoguesJsonNotFoundException
     * @param request WebRequest object WebRequest
     * @return the ApiError object
     */
    @ExceptionHandler({ PoguesJsonNotFoundException.class })
    public ResponseEntity<ApiError> handlePoguesJsonNotFoundException(
            PoguesJsonNotFoundException ex,
            WebRequest request) {
        return processException(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * Handle JSON Lunatic empty exception
     *
     * @param ex      PoguesJsonNotFoundException
     * @param request WebRequest object WebRequest
     * @return the ApiError object
     */
    @ExceptionHandler({ LunaticJsonNotFoundException.class })
    public ResponseEntity<ApiError> handleLunaticJsonNotFoundException(
            LunaticJsonNotFoundException ex,
            WebRequest request) {
        return processException(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * Handle API Exception. .
     *
     * @param ex      API Exception
     * @param request WebRequest object WebRequest
     * @return the ApiError object
     */
    @ExceptionHandler({ ApiException.class })
    public ResponseEntity<ApiError> handleApiException(
            ApiException ex,
            WebRequest request) {
        log.error(messageService.getMessage(EXCEPTION_OCCURRED_KEY), ex);
        Map<String, Object> attributes = errorAttributes.getErrorAttributes(request, ErrorAttributeOptions.defaults());
        ApiError error = errorComponent.buildErrorObject(attributes, request, ex);
        return new ResponseEntity<>(error, HttpStatus.valueOf(ex.getStatusCode()));
    }

    /**
     * Handle MissingServletRequestParameterException. Triggered when a 'required'
     * request parameter is missing.
     *
     * @param ex      MissingServletRequestParameterException
     * @param request WebRequest object WebRequest
     * @return the ApiError object
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ApiError> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            WebRequest request) {
        return processException(ex, HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is
     * invalid as well.
     *
     * @param ex      HttpMediaTypeNotSupportedException
     * @param request WebRequest object WebRequest
     * @return the ApiError object
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    protected ResponseEntity<ApiError> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            WebRequest request) {
        return processException(ex, HttpStatus.UNSUPPORTED_MEDIA_TYPE, request, messageService.getMessage(INTERNAL_EXCEPTION_KEY));
    }

    /**
     * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid
     * validation.
     *
     * @param ex      the MethodArgumentNotValidException that is thrown when @Valid
     *                validation fails
     * @param request WebRequest object WebRequest
     * @return the ApiError object
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ApiError handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        log.error(messageService.getMessage(EXCEPTION_OCCURRED_KEY), ex);
        ApiError apiError = buildErrorObject(request, HttpStatus.BAD_REQUEST, ex,
                messageService.getMessage(VALIDATION_EXCEPTION_KEY));

        List<ApiFieldError> errors = new ArrayList<>();

        List<String> messages = apiError.messages();
        for (ObjectError bindingError : ex.getBindingResult().getGlobalErrors()) {
            messages.add(messageService.getMessage(bindingError));
        }

        for (FieldError bindingError : ex.getBindingResult().getFieldErrors()) {
            ApiFieldError fieldError = new ApiFieldError(bindingError.getField(),
                    messageService.getMessage(bindingError));
            errors.add(fieldError);
        }

        apiError.addFieldErrors(errors);

        return apiError;
    }

    /**
     * Handles jakarta.validation.ConstraintViolationException.Thrown when @Validated
     * fails.
     *
     * @param ex      the ConstraintViolationException
     * @param request WebRequest object
     * @return the ApiError object
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    protected ApiError handleConstraintViolation(
            jakarta.validation.ConstraintViolationException ex,
            WebRequest request) {
        log.error(messageService.getMessage(EXCEPTION_OCCURRED_KEY), ex);
        ApiError error = buildErrorObject(request, HttpStatus.BAD_REQUEST, ex,
                messageService.getMessage(VALIDATION_EXCEPTION_KEY));
        List<ApiFieldError> violations = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            violations.add(new ApiFieldError(violation.getPropertyPath().toString(), violation.getMessage()));
        }
        error.addFieldErrors(violations);
        return error;
    }

    /**
     * Handle HttpMessageNotReadableException. Happens when request JSON is
     * malformed.
     *
     * @param ex      HttpMessageNotReadableException
     * @param request WebRequest object WebRequest
     * @return the ApiError object
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ApiError> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            WebRequest request) {
        return processException(ex, HttpStatus.BAD_REQUEST, request, messageService.getMessage(INTERNAL_EXCEPTION_KEY));
    }

    /**
     * Handle HttpMessageNotWritableException.
     *
     * @param ex      HttpMessageNotWritableException
     * @param request WebRequest object WebRequest
     * @return the ApiError object
     */
    @ExceptionHandler(HttpMessageNotWritableException.class)
    protected ResponseEntity<ApiError> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
            WebRequest request) {
        return processException(ex, HttpStatus.INTERNAL_SERVER_ERROR, request, messageService.getMessage(INTERNAL_EXCEPTION_KEY));
    }

    /**
     * Handle NoHandlerFoundException.
     *
     * @param ex NoHandlerFoundException
     * @param request WebRequest object
     * @return the ApiError object
     */

    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<ApiError> handleNoHandlerFoundException(
            NoHandlerFoundException ex, WebRequest request) {
        return processException(ex, HttpStatus.BAD_REQUEST, request, messageService.getMessage(NOTFOUND_EXCEPTION_KEY));
    }

    /**
     * Handle jakarta.persistence.EntityNotFoundException
     *
     * @param ex EntityNotFoundException
     * @param request WebRequest object
     * @return the ApiError object
     */
    @ExceptionHandler(jakarta.persistence.EntityNotFoundException.class)
    protected ResponseEntity<ApiError> handleEntityNotFound(jakarta.persistence.EntityNotFoundException ex,
            WebRequest request) {
        return processException(ex, HttpStatus.NOT_FOUND, request, messageService.getMessage(NOTFOUND_EXCEPTION_KEY));
    }

    /**
     * Handle DataIntegrityViolationException, inspects the cause for different DB
     * causes.
     *
     * @param ex      the DataIntegrityViolationException
     * @param request WebRequest object
     * @return the ApiError object
     */
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ApiError handleDataIntegrityViolation(DataIntegrityViolationException ex,
            WebRequest request) {
        log.error(messageService.getMessage(EXCEPTION_OCCURRED_KEY), ex);
        if (ex.getCause() instanceof ConstraintViolationException) {
            return buildErrorObject(request, HttpStatus.CONFLICT, ex,
                    messageService.getMessage(INTERNAL_EXCEPTION_KEY));
        }
        return buildErrorObject(request, HttpStatus.INTERNAL_SERVER_ERROR, ex,
                messageService.getMessage(INTERNAL_EXCEPTION_KEY));
    }

    /**
     * Handle Exception, handle generic Exception.class
     *
     * @param ex      the Exception
     * @param request WebRequest object
     * @return the ApiError object
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ApiError> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
            WebRequest request) {
        return processException(ex, HttpStatus.BAD_REQUEST, request, messageService.getMessage(INTERNAL_EXCEPTION_KEY));
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ApiError> handleMissingPathVariableException(WebRequest request, MissingPathVariableException ex) {
        return processException(ex, HttpStatus.BAD_REQUEST, request, messageService.getMessage(INTERNAL_EXCEPTION_KEY));
    }

    /**
     * Handle Exception. .
     *
     * @param ex      Exception
     * @param request WebRequest object WebRequest
     * @return the ApiError object
     */
    @ExceptionHandler({ Exception.class })
    protected ResponseEntity<ApiError> handleException(
            Exception ex,
            WebRequest request) {
        return processException(ex, HttpStatus.INTERNAL_SERVER_ERROR, request, messageService.getMessage(INTERNAL_EXCEPTION_KEY));
    }

    /**
     * Handle RepositoryEntity not found Exception. .
     *
     * @param ex      Exception
     * @param request WebRequest object WebRequest
     * @return the ApiError object
     */
    @ExceptionHandler({ RepositoryEntityNotFoundException.class })
    public ResponseEntity<ApiError> handleRepositoryEntityNotFoundException(
            RepositoryEntityNotFoundException ex,
            WebRequest request) {
        return processException(ex, HttpStatus.NOT_FOUND, request);
    }

    /**
     * Build ApiError object
     * @param request WebRequest object
     * @param status exception status
     * @param ex Exception
     * @param message error message
     * @return error object used as json response 
     */
    private ApiError buildErrorObject(WebRequest request, HttpStatus status, Exception ex, String message) {
        Map<String, Object> attributes = errorAttributes.getErrorAttributes(request, ErrorAttributeOptions.defaults());
        return errorComponent.buildErrorObject(attributes, request, status, ex, message);
    }
}