package ysg.fintech.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ysg.fintech.entity.AccountEntity;
import ysg.fintech.entity.MemberEntity;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {

    private int accountIdx;             // 계좌 고유번호
    private MemberEntity memberIdx;     // 회원 고유번호
    private String accNum;              // 계좌 번호
    private String accStat;             // 계좌 상태
    private LocalDate createDate;       // 계좌 개설일
    private LocalDate dropDate;         // 계좌 해지일
    private int balance;                // 계좌 잔액

    public static AccountDto fromEntity(AccountEntity accountEntity){
        return AccountDto.builder()
                .accountIdx(accountEntity.getAccountIdx())
                .memberIdx(accountEntity.getMemberIdx())
                .accNum(accountEntity.getAccNum())
                .accStat(accountEntity.getAccStat())
                .createDate(accountEntity.getCreateDate())
                .dropDate(accountEntity.getDropDate())
                .balance(accountEntity.getBalance())
                .build();
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class DropAccountDto{
        private int accountIdx;             // 계좌 고유번호
        private LocalDate dropDate;         // 계좌 해지일

    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class UpdateAccountDto{
        private int accountIdx;             // 계좌 고유번호
        private int balance;                // 계좌 잔액

    }
}
