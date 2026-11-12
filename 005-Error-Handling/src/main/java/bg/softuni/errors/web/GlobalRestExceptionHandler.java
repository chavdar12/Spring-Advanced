package bg.softuni.errors.web;

import bg.softuni.errors.product.OutOfStockException;
import bg.softuni.errors.product.ProductNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.TreeMap;

// Global handler, limited to REST controllers so the HTML pages keep their own error views.
@RestControllerAdvice(annotations = RestController.class)
public class GlobalRestExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorInfo notFound(HttpServletRequest request, ProductNotFoundException e) {
        return new ErrorInfo(request.getRequestURI(), e.getMessage());
    }

    @ExceptionHandler(OutOfStockException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorInfo outOfStock(HttpServletRequest request, OutOfStockException e) {
        return new ErrorInfo(request.getRequestURI(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorInfo invalid(HttpServletRequest request, MethodArgumentNotValidException e) {
        Map<String, String> fieldErrors = new TreeMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }
        return new ErrorInfo(request.getRequestURI(), "Validation failed.", fieldErrors);
    }
}
