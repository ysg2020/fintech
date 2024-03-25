package ysg.fintech.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ysg.fintech.Repository.AccountRepository;
import ysg.fintech.Repository.MemberRepository;
import ysg.fintech.Repository.TransRepository;
import ysg.fintech.dto.AccountDto;
import ysg.fintech.dto.MemberDto;
import ysg.fintech.entity.AccountEntity;
import ysg.fintech.entity.MemberEntity;
import ysg.fintech.exception.impl.FintechException;
import ysg.fintech.type.AccountStatus;
import ysg.fintech.type.ErrorCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountService accountService;

    // 테스트 데이터
    private AccountDto account = AccountDto.builder()
            .accountIdx(1)
            .memberIdx(1)
            .accNum("012345-01-012345")
            .accStat(AccountStatus.IN_USE)
            .createDate(LocalDate.now())
            .balance(20000)
            .build();

    @Test
    @DisplayName("존재하지않는 사용자일경우 계좌개설 불가")
    void 계좌개설실패_존재하지않는사용자() {
        //given
        given(accountRepository.findByMemberIdx(any()))
                .willReturn(new ArrayList<>());
        //when
        FintechException fintechException = Assertions.assertThrows(FintechException.class,
                () -> accountService.createAccount(account));
        //then
        Assertions.assertEquals(ErrorCode.NOT_FOUND_MEMBER,fintechException.getErrorCode());
    }

    @Test
    @DisplayName("계좌는 최대 10개까지 개설 가능")
    void 계좌개설실패_최대10개이상() {
        //given
        List<AccountEntity> accountEntityList = new ArrayList<>();
        accountEntityList.add(AccountEntity.fromDto(account));

        given(accountRepository.findByMemberIdx(any()))
                .willReturn(accountEntityList);
        given(accountRepository.countByMemberIdx(any()))
                .willReturn(10);
        //when
        FintechException fintechException = Assertions.assertThrows(FintechException.class,
                () -> accountService.createAccount(account));
        //then
        Assertions.assertEquals(ErrorCode.MAX_CREATE_ACCOUNT,fintechException.getErrorCode());
    }

    @Test
    @DisplayName("이미 해지된 계좌는 해지불가")
    void 계좌해지실패_이미해지된계좌() {
        //given
        // 계좌 해지상태로 설정
        account.setAccStat(AccountStatus.UNREGISTERED);
        given(accountRepository.findById(any()))
                .willReturn(Optional.ofNullable(AccountEntity.fromDto(account)));
        //when
        FintechException fintechException = Assertions.assertThrows(FintechException.class,
                () -> accountService.dropAccount(account));
        //then
        Assertions.assertEquals(ErrorCode.ALREADY_UNREGISTERED_ACCOUNT,fintechException.getErrorCode());

    }
    @Test
    @DisplayName("잔액이 있는 계좌는 해지불가")
    void 계좌해지실패_잔액이있는계좌() {
        //given
        // 계좌 해지상태로 설정
        given(accountRepository.findById(any()))
                .willReturn(Optional.ofNullable(AccountEntity.fromDto(account)));
        //when
        FintechException fintechException = Assertions.assertThrows(FintechException.class,
                () -> accountService.dropAccount(account));
        //then
        Assertions.assertEquals(ErrorCode.NOT_EMPTY_BALANCE,fintechException.getErrorCode());

    }


}
