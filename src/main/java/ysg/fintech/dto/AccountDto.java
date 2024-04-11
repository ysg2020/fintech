package ysg.fintech.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ysg.fintech.entity.AccountEntity;
import ysg.fintech.type.AccountStatus;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {

    private int accountIdx;             // 계좌 고유번호

    @NotNull
    private int memberIdx;              // 회원 고유번호

    @Size(min = 12,max = 16)
    private String accNum;              // 계좌 번호

    @NotNull
    private AccountStatus accStat;      // 계좌 상태

    @PastOrPresent
    private LocalDate createDate;       // 계좌 개설일

    @PastOrPresent
    private LocalDate dropDate;         // 계좌 해지일

    @PositiveOrZero
    private int balance;                // 계좌 잔액

    public static AccountDto fromEntity(AccountEntity accountEntity){
        return AccountDto.builder()
                .accountIdx(accountEntity.getAccountIdx())
                .memberIdx(accountEntity.getMemberIdx().getMemberIdx())
                .accNum(accountEntity.getAccNum())
                .accStat(accountEntity.getAccStat())
                .createDate(accountEntity.getCreateDate())
                .dropDate(accountEntity.getDropDate())
                .balance(accountEntity.getBalance())
                .build();
    }

}
