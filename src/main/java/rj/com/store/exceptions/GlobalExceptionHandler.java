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


@RestControllerAdvice
@Tag(name = "Global Exception handler ",description = "It return all type of errors in Api")
public class GlobalExceptionHandler {
   private final Logger logger= LoggerFactory.getLogger(GlobalExceptionHandler.class);
   //Handel Resource Not Found Exception
    @ExceptionHandler(ResourceNotFoundException.class)
    @Operation(summary = "It any resource are not found it execute")
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
    //Handel Method Argument Not ValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @Operation(summary = "If any bad as a argument request invoked it is execute")
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

    //Handel Bad Api Request Exception
    @ExceptionHandler(BadApiRequest.class)
    @Operation(summary = "If any bad request invoked it is execute")
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
    @ExceptionHandler(BadCredentialsException.class)
    @Operation(summary = "If user are not authentic then it will be execute")
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


}
