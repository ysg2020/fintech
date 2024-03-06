package ysg.fintech.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ysg.fintech.dto.MemberDto;

@Entity(name = "member")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberEntity {

    @Id
    @Column(name = "member_idx")
    private int memberIdx;      // 회원 고유번호

    @Column(name = "user_id")
    private String userId;      // 회원 아이디

    @Column(name = "user_pwd")
    private String userPwd;     // 회원 비밀번호

    private String name;        // 이름
    private String gender;      // 성별
    private String phone;       // 전화번호

    public static MemberEntity fromDto(MemberDto memberDto){
        return MemberEntity.builder()
                .memberIdx(memberDto.getMemberIdx())
                .userId(memberDto.getUserId())
                .userPwd(memberDto.getUserPwd())
                .name(memberDto.getName())
                .gender(memberDto.getGender())
                .phone(memberDto.getPhone())
                .build();
    }


}
