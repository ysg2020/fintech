package ysg.fintech.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ysg.fintech.entity.MemberEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {

    private int memberIdx;  // 회원 고유번호
    private String userId;  // 회원 아이디
    private String userPwd; // 회원 비밀번호
    private String name;    // 이름
    private String gender;  // 성별
    private String phone;   // 전화번호

    public static MemberDto fromEntity(MemberEntity memberEntity){
        return MemberDto.builder()
                .memberIdx(memberEntity.getMemberIdx())
                .userId(memberEntity.getUserId())
                .userPwd(memberEntity.getUserPwd())
                .name(memberEntity.getName())
                .gender(memberEntity.getGender())
                .phone(memberEntity.getPhone())
                .build();
    }


}
