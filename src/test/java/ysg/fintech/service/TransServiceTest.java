package ysg.fintech.service;

import org.junit.jupiter.api.Assertions;
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
import ysg.fintech.dto.TransDto;
import ysg.fintech.entity.AccountEntity;
import ysg.fintech.entity.MemberEntity;
import ysg.fintech.entity.TransEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TransServiceTest {
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransRepository transRepository;

    @InjectMocks
    private TransService transService;

    // 테스트 데이터
    private MemberDto member = MemberDto.builder()
            .memberIdx(1)
            .userId("test12")
            .userPwd("pwd12")
            .name("테스터")
            .gender("M")
            .phone("010-1234-5678")
            .build();
    private AccountDto account = AccountDto.builder()
            .accountIdx(1)
            .memberIdx(MemberEntity.fromDto(member))
            .accNum("012345-01-012345")
            .accStat("IN_USE")
            .createDate(LocalDate.now())
            .balance(0)
            .build();

    private TransDto trans = TransDto.builder()
            .transIdx(1)
            .memberIdx(MemberEntity.fromDto(member))
            .accountIdx(AccountEntity.fromDto(account))
            .transType("DEPOSIT")
            .transStat("S")
            .amount(10000)
            .transDate(LocalDateTime.now())
            .build();

    @Test
    void 입금요청() {
        //given
        // 저장되기 전의 값을 찾기 위해 사용 (실제 검증)
        ArgumentCaptor<AccountEntity> captor = ArgumentCaptor.forClass(AccountEntity.class);
        // account 조회 모킹
        given(accountRepository.findById(any()))
                .willReturn(Optional.ofNullable(AccountEntity.fromDto(account)));
        // trans 저장 모킹
        given(transRepository.save(any()))
                .willReturn(TransEntity.fromDto(trans));
        //when
        transService.createTransaction(trans);
        //then
        // 저장 되기전 변경된 잔액 정보 확인 (입금되었는지 확인)
        verify(accountRepository,times(1)).save(captor.capture());
        // 잔액이 0원인 계좌에 1만원을 입금했으므로 1만원을 예상
        Assertions.assertEquals(captor.getValue().getBalance(),10000);
    }

    @Test
    void 출금요청성공() {
        //given
        // 잔액이 1만원인 계좌로 설정
        account.setBalance(10000);
        // 거래 종류 출금으로 설정
        trans.setTransType("WITHDRAWAL");
        // 저장되기 전의 값을 찾기 위해 사용 (실제 검증)
        ArgumentCaptor<AccountEntity> captor = ArgumentCaptor.forClass(AccountEntity.class);
        // account 조회 모킹
        given(accountRepository.findById(any()))
                .willReturn(Optional.ofNullable(AccountEntity.fromDto(account)));
        // trans 저장 모킹
        given(transRepository.save(any()))
                .willReturn(TransEntity.fromDto(trans));
        //when
        transService.createTransaction(trans);
        //then
        // 저장 되기전 변경된 잔액 정보 확인 (출금되었는지 확인)
        verify(accountRepository,times(1)).save(captor.capture());
        // 잔액이 1만원인 계좌에 1만원을 출금했으므로 0원임을 예상
        Assertions.assertEquals(captor.getValue().getBalance(),0);

    }

    @Test
    void 출금요청실패_잔액부족() {
        //given
        // 거래 종류 출금으로 설정
        trans.setTransType("WITHDRAWAL");
        // 거래 금액을 2만원으로 설정
        trans.setAmount(20000);
        // account 조회 모킹
        given(accountRepository.findById(any()))
                .willReturn(Optional.ofNullable(AccountEntity.fromDto(account)));
        //when
        //then
        // 저장 되기전 변경된 잔액 정보 확인 (입금되었는지 확인)
        // 잔액이 0원인 계좌에 2만원을 출금시도했으므로 예외 발생을 예상
        Assertions.assertThrows(RuntimeException.class,()->transService.createTransaction(trans));
    }

    @Test
    void 송금성공() {
        //given
        //when
        //then

    }



}
