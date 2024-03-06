package ysg.fintech.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import ysg.fintech.Repository.TransRepository;
import ysg.fintech.entity.AccountEntity;
import ysg.fintech.entity.MemberEntity;
import ysg.fintech.entity.TransEntity;

import java.time.LocalDateTime;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransRepositoryTest {

    @Autowired
    private TransRepository transRepository;

    @Test
    @DisplayName("거래 생성")
    @Rollback(value = false)
    void 거래생성() {
        //given
        TransEntity trans = TransEntity.builder()
                .accountIdx(AccountEntity.builder()
                        .accountIdx(1)
                        .build())
                .memberIdx(MemberEntity.builder()
                        .memberIdx(1)
                        .build())
                .transType("S")
                .transStat("입금")
                .amount(1000)
                .transDate(LocalDateTime.now())
                .build();
        //when
        TransEntity saveTrans = transRepository.save(trans);
        //then
        Assertions.assertEquals(trans.getAmount(),saveTrans.getAmount());

    }
}
