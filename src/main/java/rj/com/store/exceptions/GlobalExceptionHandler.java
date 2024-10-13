package rj.com.store.exceptions;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import rj.com.store.datatransferobjects.ApiResponseMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Global Exception Handler for managing various exceptions thrown within the application.
 * This class provides centralized handling of exceptions and returns appropriate HTTP responses
 * along with messages in a standardized format.
 *
 * It handles:
 * - ResourceNotFoundException
 * - MethodArgumentNotValidException
 * - BadApiRequest
 * - BadCredentialsException
 * - IllegalArgumentException
 *
 * The responses are formatted as ApiResponseMessage objects, encapsulating success status,
 * message, and HTTP status code.
 */
@RestControllerAdvice
@Tag(name = "Global Exception handler", description = "Returns all types of errors in the API")
public class GlobalExceptionHandler {
   private final Logger logger= LoggerFactory.getLogger(GlobalExceptionHandler.class);
    /**
     * Handles ResourceNotFoundException thrown when a requested resource is not found.
     *
     * @param exception the ResourceNotFoundException instance
     * @return a ResponseEntity containing an ApiResponseMessage with a NOT FOUND status
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @Operation(summary = "Handles any resource not found exception")
    public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException exception){
        logger.info("Execution Handler invoked !! Exception is {}",exception.getMessage());
        return new ResponseEntity<>(
                ApiResponseMessage.builder()
                        .massage(exception.getMessage())
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .success(true)
                        .build()
                ,HttpStatus.NOT_FOUND);
    }
    /**
     * Handles MethodArgumentNotValidException thrown when request arguments are invalid.
     *
     * @param exception the MethodArgumentNotValidException instance
     * @return a ResponseEntity containing a map of field errors with a BAD REQUEST status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @Operation(summary = "Handles bad argument requests")
    public ResponseEntity<Map<String , Object>>  handlerMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        List<ObjectError> errors= exception.getBindingResult().getAllErrors();
        Map<String,Object> response=new HashMap<>();
        errors.stream().forEach(objectError->{
            String message =objectError.getDefaultMessage();
            String fieldName=((FieldError)objectError).getField();

            response.put(fieldName,message);
        });
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
    /**
     * Handles BadApiRequest thrown for invalid requests.
     *
     * @param exception the BadApiRequest instance
     * @return a ResponseEntity containing an ApiResponseMessage with a BAD REQUEST status
     */
    @ExceptionHandler(BadApiRequest.class)
    @Operation(summary = "Handles bad API requests")
    public ResponseEntity<ApiResponseMessage> badApiRequestExceptionHandler(BadApiRequest exception){
        logger.info("Execution Handler invoked !! Exception is {}",exception.getMessage());
        return new ResponseEntity<>(
                ApiResponseMessage.builder()
                        .massage(exception.getMessage())
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .success(false)
                        .build()
                ,HttpStatus.BAD_REQUEST);
    }
    /**
     * Handles BadCredentialsException thrown when authentication fails.
     *
     * @param exception the BadCredentialsException instance
     * @return a ResponseEntity containing an ApiResponseMessage with a NOT FOUND status
     */
    @ExceptionHandler(BadCredentialsException.class)
    @Operation(summary = "Handles failed user authentication")
    public ResponseEntity<ApiResponseMessage> badCredentialsExceptionHandler(BadCredentialsException exception){
        logger.info("Execution Handler invoked !! Exception is {}",exception.getMessage());
        return new ResponseEntity<>(
                ApiResponseMessage.builder()
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .massage(exception.getMessage())
                        .success(true)
                        .build(),HttpStatus.NOT_FOUND
        );
    }
    /**
     * Handles IllegalArgumentException thrown for invalid DTOs.
     *
     * @param exception the IllegalArgumentException instance
     * @return a ResponseEntity containing an ApiResponseMessage with a BAD REQUEST status
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @Operation(summary = "Handles invalid DTOs or null data")
    public ResponseEntity<ApiResponseMessage> illegalArgumentExceptionHandler(IllegalArgumentException exception){
        logger.info("Execution Handler invoked !! Exception is {}",exception.getMessage());
        return new ResponseEntity<>(
                ApiResponseMessage.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .massage("DTO is null or contains invalid data")
                        .success(false)
                        .build(),HttpStatus.BAD_REQUEST);
    }

}
