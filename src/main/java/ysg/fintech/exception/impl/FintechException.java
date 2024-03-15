package ysg.fintech.exception.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import ysg.fintech.exception.FintechAbstractException;
import ysg.fintech.type.ErrorCode;

@Getter
@AllArgsConstructor
public class FintechException extends FintechAbstractException {

    private ErrorCode errorCode;

}
