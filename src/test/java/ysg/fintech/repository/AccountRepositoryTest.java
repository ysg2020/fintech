package ysg.fintech.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import ysg.fintech.Repository.AccountRepository;
import ysg.fintech.entity.AccountEntity;
import ysg.fintech.entity.MemberEntity;

import java.time.LocalDate;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    @DisplayName("계좌 생성")
    @Rollback(value = false)
    void 계좌생성() {
        //given
        AccountEntity createAccount = AccountEntity.builder()
                .memberIdx(MemberEntity.builder()
                        .memberIdx(1)
                        .build())
                .accNum("012345-01-012345")
                .accStat("IN_USE")
                .createDate(LocalDate.now())
                .balance(0)
                .build();
        //when
        AccountEntity saveAccount = accountRepository.save(createAccount);
        //then
        Assertions.assertEquals(createAccount.getAccNum(),saveAccount.getAccNum());

    }


    @Test
    @DisplayName("계좌 해지")
    @Rollback(value = false)
    void 계좌해지() {
        //given
        AccountEntity unregisteredAccount = AccountEntity.builder()
                .accountIdx(1)
                .memberIdx(MemberEntity.builder()
                        .memberIdx(1)
                        .build())
                .accNum("012345-01-012345")
                .accStat("UNREGISTERED")
                .createDate(LocalDate.now())
                .dropDate(LocalDate.now())
                .balance(0)
                .build();
        //when
        AccountEntity saveAccount = accountRepository.save(unregisteredAccount);
        //then
        Assertions.assertEquals(saveAccount.getAccStat(),"UNREGISTERED");
    }


}
