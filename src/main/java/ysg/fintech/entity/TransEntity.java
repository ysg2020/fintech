package ysg.fintech.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ysg.fintech.dto.TransDto;
import ysg.fintech.type.TransStatus;
import ysg.fintech.type.TransType;

import java.time.LocalDateTime;

@Entity(name = "trans")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransEntity {

    @Id
    @Column(name = "trans_idx")
    private int transIdx;               // 거래 고유번호

    @ManyToOne
    @JoinColumn(name = "account_idx")
    private AccountEntity accountIdx;   // 계좌 고유번호

    @ManyToOne
    @JoinColumn(name = "member_idx")
    private MemberEntity memberIdx;     // 회원 고유번호

    @Column(name = "trans_type")
    @Enumerated(EnumType.STRING)
    private TransType transType;           // 거래 종류

    @Column(name = "trans_stat")
    @Enumerated(EnumType.STRING)
    private TransStatus transStat;           // 거래 상태

    private int amount;              // 거래 금액

    @Column(name = "trans_date")
    private LocalDateTime transDate;           // 거래 일시

    @Column(name = "trans_target")
    private String transTarget;         // 거래 대상자명

    @Column(name = "trans_target_acc_num")
    private String transTargetAccNum;         // 거래 대상자 계좌번호
    public static TransEntity fromDto(TransDto transDto){
        return TransEntity.builder()
                .transIdx(transDto.getTransIdx())
                .accountIdx(transDto.getAccountIdx())
                .memberIdx(transDto.getMemberIdx())
                .transType(transDto.getTransType())
                .transStat(transDto.getTransStat())
                .amount(transDto.getAmount())
                .transDate(transDto.getTransDate())
                .transTarget(transDto.getTransTarget())
                .transTargetAccNum(transDto.getTransTargetAccNum())
                .build();
    }

    // 거래 취소 설정
    public void cancelTrans(){
        this.transStat = TransStatus.CANCEL;
    }
}
