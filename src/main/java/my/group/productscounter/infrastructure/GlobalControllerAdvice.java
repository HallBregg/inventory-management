package my.group.productscounter.infrastructure;


import jakarta.servlet.http.HttpServletRequest;
import my.group.productscounter.project.exception.ProjectServiceException;
import my.group.productscounter.store.exception.ProductStoreServiceException;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Order(1)
@ControllerAdvice
class GlobalControllerAdvice {


    @ExceptionHandler(ProjectServiceException.class)
    ResponseEntity<ErrorResponse> onProjectServiceException(ProjectServiceException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getCode(), ex.getMessage(), null));
    }

    @ExceptionHandler(ProductStoreServiceException.class)
    ResponseEntity<ErrorResponse> onProductStoreServiceException(ProductStoreServiceException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getCode(), ex.getMessage(), null));
    }

    // 1. Resource is not found 404
    @ExceptionHandler(NoResourceFoundException.class)
    ResponseEntity<ErrorResponse> onNoResourceFoundException(NoResourceFoundException exc, HttpServletRequest request){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        "NOT_FOUND",
                        String.format("No handler found for %s %s", request.getMethod(), request.getRequestURI()),
                        exc.getMessage()));
    }

//     2. JPA / DataIntegrity violations (constraint errors).
//     DB errors are challenging to handle from Transactional annotation in the Service layer.
//     Maybe it would be a good idea to somehow catch them on the service layer.
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> onDataIntegrityViolationException(DataIntegrityViolationException exc) {
        String details = exc.getRootCause() != null
                ? exc.getRootCause().getMessage()
                : exc.getMessage();

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        "DATA_INTEGRITY_VIOLATION",
                        "Database constraint violation",
                        details
                ));
    }

    // 4. Method isn't supported
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> onHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exc) {
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new ErrorResponse(
                        "METHOD_NOT_ALLOWED",
                        exc.getMessage(),
                        null
                ));
    }

    // 5. Media type isn't supported
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> onHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exc) {
        return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(new ErrorResponse(
                        "UNSUPPORTED_MEDIA_TYPE",
                        exc.getMessage(),
                        null
                ));
    }

    // 6. Media type is not acceptable
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ErrorResponse> onHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_ACCEPTABLE)
            .body(new ErrorResponse(
                    "NOT_ACCEPTABLE",
                    ex.getMessage(),
                    null
            ));
    }

    // 7. Invalid JSON
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> onHttpMessageNotReadableException(HttpMessageNotReadableException exc){
        return ResponseEntity
            .badRequest()
            .body(new ErrorResponse(
                    "MALFORMED_JSON",
                    "Malformed JSON request",
                    exc.getMostSpecificCause().getMessage()));
    }

    // 8. Invalid request path params.
    @ExceptionHandler({
        MissingServletRequestParameterException.class,
        MissingPathVariableException.class,
        MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ErrorResponse> handleMissingOrTypeMismatch(Exception ex) {
        String code = switch (ex.getClass().getSimpleName()) {
            case "MissingServletRequestParameterException" -> "MISSING_PARAMETER";
            case "MissingPathVariableException"            -> "MISSING_PATH_VARIABLE";
            default                                        -> "TYPE_MISMATCH";
        };
        return ResponseEntity
            .badRequest()
            .body(new ErrorResponse(code, ex.getMessage(), null));
    }

    // 9. Validation
    @ExceptionHandler({
        ConstraintViolationException.class,
        BindException.class
    })
    public ResponseEntity<ErrorResponse> handleParameterErrors(Exception ex) {
        Map<String, List<String>> errors;
        if (ex instanceof ConstraintViolationException cve) {
            errors = cve.getConstraintViolations().stream()
                .collect(Collectors.groupingBy(
                    cv -> cv.getPropertyPath().toString(),
                    LinkedHashMap::new,
                    Collectors.mapping(ConstraintViolation::getMessage, Collectors.toList())
                ));
        } else {
            errors = ((BindException) ex).getFieldErrors().stream()
                .collect(Collectors.groupingBy(
                    FieldError::getField,
                    LinkedHashMap::new,
                    Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
                ));
        }
        return ResponseEntity
            .badRequest()
            .body(new ErrorResponse(
                    "VALIDATION_FAILED",
                    "Parameter validation failed",
                    errors));
    }

    // 10. RequestBody validation errors (e.g. @Valid DTOs)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(
            MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = ex.getBindingResult()
            .getFieldErrors().stream()
            .collect(Collectors.groupingBy(
                FieldError::getField,
                LinkedHashMap::new,
                Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
            ));
        return ResponseEntity
            .badRequest()
            .body(new ErrorResponse(
                    "VALIDATION_FAILED",
                    "Request body validation failed",
                    errors));
    }
}
