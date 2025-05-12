package my.group.productscounter.project;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


record ErrorResponse(String code, String message) {}

@ControllerAdvice(assignableTypes = ProjectController.class)
class ProjectControllerExceptionHandler {

    @ExceptionHandler(ProjectNotFoundException.class)
    ResponseEntity<Object> handleProjectNotFoundException(ProjectNotFoundException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getCode(), "Product Not Found"), HttpStatus.NOT_FOUND);
    }
}
