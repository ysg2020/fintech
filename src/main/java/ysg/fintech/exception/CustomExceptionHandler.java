package ysg.fintech.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ysg.fintech.exception.impl.FintechException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FintechException.class)
    public ErrorResponse fintechException(FintechException e){
        return new ErrorResponse(e.getErrorCode(),e.getMessage());
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ResponseEntity handleValidateException(BindException ex){

        Map<String,String> errors = new HashMap<>();
        ex.getAllErrors().forEach((error)->{
            String filedName = ((FieldError)error).getField();
            String message = error.getDefaultMessage();
            errors.put(filedName,message);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


}
