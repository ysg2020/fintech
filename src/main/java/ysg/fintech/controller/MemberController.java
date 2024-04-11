package ysg.fintech.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ysg.fintech.dto.MemberDto;
import ysg.fintech.service.MemberService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    // 회원 가입
    @PostMapping
    public MemberDto signUp(@RequestBody @Validated MemberDto memberDto){
        return memberService.signUp(memberDto);
    }

    // 회원 탈퇴
    @DeleteMapping
    public void signOut(@RequestBody @Validated MemberDto memberDto){
        memberService.signOut(memberDto);
    }
}
