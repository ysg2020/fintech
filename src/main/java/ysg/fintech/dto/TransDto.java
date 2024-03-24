package ysg.fintech.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ysg.fintech.entity.AccountEntity;
import ysg.fintech.entity.MemberEntity;
import ysg.fintech.entity.TransEntity;
import ysg.fintech.type.TransStatus;
import ysg.fintech.type.TransType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransDto {

    private int transIdx;                       // 거래 고유번호
    private int accountIdx;                     // 계좌 고유번호
    private int memberIdx;                      // 회원 고유번호
    private TransType transType;                // 거래 종류
    private TransStatus transStat;              // 거래 상태
    private int amount;                         // 거래 금액

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime transDate;            // 거래 일시

    private String transTarget;                 // 거래 대상자명
    private String transTargetAccNum;           // 거래 대상자 계좌번호

    public static TransDto fromEntity(TransEntity transEntity){
        return TransDto.builder()
                .transIdx(transEntity.getTransIdx())
                .accountIdx(transEntity.getAccountIdx().getAccountIdx())
                .memberIdx(transEntity.getMemberIdx().getMemberIdx())
                .transType(transEntity.getTransType())
                .transStat(transEntity.getTransStat())
                .amount(transEntity.getAmount())
                .transDate(transEntity.getTransDate())
                .transTarget(transEntity.getTransTarget())
                .transTarget(transEntity.getTransTargetAccNum())
                .build();
    }
}
