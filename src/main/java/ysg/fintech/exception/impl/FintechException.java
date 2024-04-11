package ysg.fintech.exception.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ysg.fintech.type.ErrorCode;

@Getter
public class FintechException extends RuntimeException {

    private ErrorCode errorCode;
    private String message;

    public FintechException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }
}
