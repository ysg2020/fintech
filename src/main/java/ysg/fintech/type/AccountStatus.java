package ysg.fintech.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountStatus {

    IN_USE("사용 중인 계좌"),
    UNREGISTERED("해지한 계좌");

    private String message;

}
