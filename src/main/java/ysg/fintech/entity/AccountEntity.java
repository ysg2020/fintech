package ysg.fintech.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ysg.fintech.dto.AccountDto;
import ysg.fintech.exception.impl.FintechException;
import ysg.fintech.type.AccountStatus;
import ysg.fintech.type.ErrorCode;

import java.time.LocalDate;

@Entity(name = "account")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "account", indexes = @Index(name = "idx_acc_num",columnList = "acc_num"))
public class AccountEntity {
    
    @Id
    @Column(name = "account_idx")
    private int accountIdx;                 // 계좌 고유번호

    @ManyToOne
    @JoinColumn(name = "member_idx")
    private MemberEntity memberIdx;         // 회원 고유번호

    @Column(name = "acc_num")
    private String accNum;                  // 계좌 번호

    @Column(name = "acc_stat")
    @Enumerated(EnumType.STRING)
    private AccountStatus accStat;          // 계좌 상태

    @Column(name = "create_date")
    private LocalDate createDate;           // 계좌 개설일

    @Column(name = "drop_date")
    private LocalDate dropDate;             // 계좌 해지일

    private int balance;                    // 계좌 잔액

    public static AccountEntity fromDto(AccountDto accountDto){
        return AccountEntity.builder()
                .accountIdx(accountDto.getAccountIdx())
                .memberIdx(MemberEntity.builder()
                        .memberIdx(accountDto.getMemberIdx())
                        .build())
                .accNum(accountDto.getAccNum())
                .accStat(accountDto.getAccStat())
                .createDate(accountDto.getCreateDate())
                .dropDate(accountDto.getDropDate())
                .balance(accountDto.getBalance())
                .build();
    }

    // 계좌 해지 설정
    public void dropAccount(AccountDto accountDto){
        this.accStat = AccountStatus.UNREGISTERED;
        this.dropDate = accountDto.getDropDate();
    }

    // 입금
    public void deposit(int amount){
        if(amount < 0){
            throw new FintechException(ErrorCode.INVALID_DEPOSIT);
        }
        balance += amount;
    }

    // 출금
    public void withdrawal(int amount){
        if(amount > balance){
            throw new FintechException(ErrorCode.NOT_ENOUGH_BALANCE);
        }
        balance -= amount;
    }



}
