package ysg.fintech.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransStatus {
    SUCCESS("거래 성공"),
    CANCEL("거래 취소");

    private String message;

}
