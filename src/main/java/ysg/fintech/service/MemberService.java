package ysg.fintech.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ysg.fintech.Repository.MemberRepository;
import ysg.fintech.dto.MemberDto;
import ysg.fintech.entity.MemberEntity;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;


    // 회원 가입
    public MemberDto signUp(MemberDto memberDto){
        // dto > entity 로 변환
        MemberEntity member = MemberEntity.fromDto(memberDto);
        // entity > dto 로 변환 후 리턴
        return MemberDto.fromEntity(memberRepository.save(member));
    }

    // 회원 탈퇴
    public void signDown(MemberDto memberDto){
        // dto > entity 로 변환
        MemberEntity member = MemberEntity.fromDto(memberDto);

        memberRepository.delete(member);
    }
}
