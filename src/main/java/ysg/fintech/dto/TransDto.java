package ysg.fintech.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ysg.fintech.entity.AccountEntity;
import ysg.fintech.entity.MemberEntity;
import ysg.fintech.entity.TransEntity;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransDto {

    private int transIdx;               // 거래 고유번호
    private AccountEntity accountIdx;   // 계좌 고유번호
    private MemberEntity memberIdx;     // 회원 고유번호
    private String transType;           // 거래 종류
    private String transStat;           // 거래 상태
    private int amount;              // 거래 금액
    private LocalDateTime transDate;           // 거래 일시
    private String transTarget;         // 거래 대상자명

    public static TransDto fromEntity(TransEntity transEntity){
        return TransDto.builder()
                .transIdx(transEntity.getTransIdx())
                .accountIdx(transEntity.getAccountIdx())
                .memberIdx(transEntity.getMemberIdx())
                .transType(transEntity.getTransType())
                .transStat(transEntity.getTransStat())
                .amount(transEntity.getAmount())
                .transDate(transEntity.getTransDate())
                .transTarget(transEntity.getTransTarget())
                .build();
    }
}
