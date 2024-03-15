package ysg.fintech.exception;

import lombok.Builder;
import lombok.Data;
import ysg.fintech.type.ErrorCode;

@Data
@Builder
public class ErrorResponse {
    private ErrorCode code;
    private String message;
}
