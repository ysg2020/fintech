package ysg.fintech.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import ysg.fintech.Repository.MemberRepository;
import ysg.fintech.entity.MemberEntity;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Test
    @DisplayName("회원 등록")
    @Rollback(value = false)
    void 회원등록() {
        //given
        MemberEntity member = MemberEntity.builder()
                .userId("test12")
                .userPwd("pwd12")
                .name("테스터")
                .gender("M")
                .phone("010-1234-5678")
                .build();
        //when
        MemberEntity saveMember = memberRepository.save(member);
        //then
        Assertions.assertEquals(member.getName(),saveMember.getName());
    }
}
