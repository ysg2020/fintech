package ysg.fintech.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ysg.fintech.exception.impl.FintechException;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FintechException.class)
    public ErrorResponse fintechException(FintechException e){
        return new ErrorResponse(e.getErrorCode(),e.getMessage());
    }
}
