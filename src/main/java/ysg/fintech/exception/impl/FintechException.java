package ysg.fintech.exception.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ysg.fintech.type.ErrorCode;

@Getter
@AllArgsConstructor
public class FintechException extends RuntimeException {

    private ErrorCode errorCode;

}
