package my.group.productscounter.store;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

record ErrorResponse(Integer code, String message) {}

@ControllerAdvice(assignableTypes = ProductController.class)
class ProductControllerExceptionHandler {

    @ExceptionHandler(ProductCouldNotBeCreatedException.class)
    ResponseEntity<Object> handleProductCouldNotBeCreatedException(ProductCouldNotBeCreatedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    ResponseEntity<Object> handleProductNotFoundException(ProductNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(1, "Product Not Found"));
    }
}
