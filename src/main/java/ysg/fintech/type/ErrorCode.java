package ysg.fintech.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    MAX_CREATE_ACCOUNT("계좌는 최대 10개까지 개설 가능합니다."),
    NOT_EMPTY_BALANCE("잔액이 있는 계좌는 해지할 수 없습니다."),
    ALREADY_UNREGISTERED_ACCOUNT("계좌가 이미 해지되어 있습니다"),
    NOT_FOUND_ACCOUNT("해당 계좌가 존재하지 않습니다."),
    CAN_NOT_TRANS_UNREGISTERED_ACCOUNT("해지되어있는 계좌는 거래 할수 없습니다."),
    NOT_ENOUGH_BALANCE("잔액이 충분하지 않습니다."),
    TOO_OLD_CANCEL("기간이 지난 거래는 취소 불가능합니다."),
    NOT_FOUND_MEMBER("해당 사용자가 존재하지 않습니다."),
    INVALID_TRANS_TYPE("잘못된 거래 종류입니다."),
    INVALID_DEPOSIT("잘못된 입금 금액 입니다."),
    NOT_FOUND_TRANS("해당 거래가 존재하지 않습니다."),
    ALREADY_CANCEL_TRANS("거래가 이미 취소되어 있습니다.");


    private String message;

}
