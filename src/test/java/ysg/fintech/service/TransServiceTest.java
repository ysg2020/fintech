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
import ysg.fintech.dto.TransDto;
import ysg.fintech.entity.AccountEntity;
import ysg.fintech.entity.MemberEntity;
import ysg.fintech.entity.TransEntity;
import ysg.fintech.exception.impl.FintechException;
import ysg.fintech.type.AccountStatus;
import ysg.fintech.type.ErrorCode;
import ysg.fintech.type.TransStatus;
import ysg.fintech.type.TransType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
            .accStat(AccountStatus.IN_USE)
            .createDate(LocalDate.now())
            .balance(20000)
            .build();
    private MemberDto targetMember = MemberDto.builder()
            .memberIdx(2)
            .userId("test34")
            .userPwd("pwd34")
            .name("테스터2")
            .gender("F")
            .phone("010-4321-8765")
            .build();
    private AccountDto targetAccount = AccountDto.builder()
            .accountIdx(1)
            .memberIdx(MemberEntity.fromDto(member))
            .accNum("123456-02-123456")
            .accStat(AccountStatus.IN_USE)
            .createDate(LocalDate.now())
            .balance(100000)
            .build();
    private TransDto trans = TransDto.builder()
            .transIdx(1)
            .memberIdx(MemberEntity.fromDto(member))
            .accountIdx(AccountEntity.fromDto(account))
            .transType(TransType.DEPOSIT)
            .transStat(TransStatus.SUCCESS)
            .amount(10000)
            .transDate(LocalDateTime.now())
            .build();


    @Test
    @DisplayName("계좌가 존재하지 않는경우 거래실패")
    void 거래실패_계좌X() {
        //given
        given(accountRepository.findById(any()))
                .willReturn(Optional.empty());
        //when
        FintechException fintechException = Assertions.assertThrows(FintechException.class,
                () -> transService.createTransaction(trans));
        //then
        // 존재하지 않는 계좌 예외가 발생함을 예상
        Assertions.assertEquals(ErrorCode.NOT_FOUND_ACCOUNT,fintechException.getErrorCode());

    }
    @Test
    @DisplayName("해지된 계좌인경우 거래실패")
    void 거래실패_해지계좌() {
        //given
        // 해지계좌로 설정
        account.setAccStat(AccountStatus.UNREGISTERED);
        // account 조회 모킹
        given(accountRepository.findById(any()))
                .willReturn(Optional.ofNullable(AccountEntity.fromDto(account)));
        //when
        FintechException fintechException = Assertions.assertThrows(FintechException.class,
                () -> transService.createTransaction(trans));
        //then
        // 존재하지 않는 계좌 예외가 발생함을 예상
        Assertions.assertEquals(ErrorCode.CAN_NOT_TRANS_UNREGISTERED_ACCOUNT,fintechException.getErrorCode());

    }
    @Test
    @DisplayName("잔액 2만원인 계좌에 1만원 입금했으므로 총 3만원이어야한다")
    void 입금요청성공() {
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
        // 잔액이 2만원인 계좌에 1만원을 입금했으므로 3만원을 예상
        Assertions.assertEquals(captor.getValue().getBalance(),30000);
    }

    @Test
    @DisplayName("잔액 2만원인 계좌에 1만원 출금했으므로 총 1만원이어야한다")
    void 출금요청성공() {
        //given
        // 거래 종류 출금으로 설정
        trans.setTransType(TransType.WITHDRAWAL);
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
        // 잔액이 2만원인 계좌에 1만원을 출금했으므로 1만원임을 예상
        Assertions.assertEquals(captor.getValue().getBalance(),10000);

    }


    @Test
    @DisplayName("잔액 2만원인 계좌에 5만원 출금시도했으므로 출금 실패한다")
    void 출금요청실패_잔액부족() {
        //given
        // 거래 종류 출금으로 설정
        trans.setTransType(TransType.WITHDRAWAL);
        // 거래 금액을 5만원으로 설정
        trans.setAmount(50000);
        // account 조회 모킹
        given(accountRepository.findById(any()))
                .willReturn(Optional.ofNullable(AccountEntity.fromDto(account)));
        //when
        FintechException fintechException = Assertions.assertThrows(FintechException.class,
                () -> transService.createTransaction(trans));
        //then
        // 저장 되기전 변경된 잔액 정보 확인 (입금되었는지 확인)
        // 잔액이 2만원인 계좌에 5만원을 출금시도했으므로 잔액 부족 예외 발생을 예상
        Assertions.assertEquals(ErrorCode.NOT_ENOUGH_BALANCE,fintechException.getErrorCode());

    }

    @Test
    @DisplayName("1만원을 송금했으므로 각각 계좌에 1만원씩 입금 및 출금이 되어야한다")
    void 송금성공() {
        //given
        // 거래 종류 송금으로 설정
        trans.setTransType(TransType.TRANS);
        // 저장되기 전의 값을 찾기 위해 사용 (실제 검증)
        ArgumentCaptor<AccountEntity> captor = ArgumentCaptor.forClass(AccountEntity.class);
        // account 조회 모킹
        given(accountRepository.findById(any()))
                .willReturn(Optional.ofNullable(AccountEntity.fromDto(account)));
        // trans 저장 모킹
        given(transRepository.save(any()))
                .willReturn(TransEntity.fromDto(trans));
        // targetAccount 조회 모킹
        given(accountRepository.findByAccNum(any()))
                .willReturn(Optional.ofNullable(AccountEntity.fromDto(targetAccount)));
        //when
        transService.createTransaction(trans);
        //then
        // 저장 되기전 변경된 잔액 정보 확인 (송금되었는지 확인)
        verify(accountRepository,times(2)).save(captor.capture());
        // 송금하는 금액은 1만원이고 거래대상자의 계좌 잔액은 10만원이므로 송금 이후 11만원임을 예상
        // 송금 후 남아있는 계좌 잔액은 1만원임을 예상
        List<AccountEntity> allValues = captor.getAllValues();
        Assertions.assertEquals(allValues.get(0).getBalance(),110000);
        Assertions.assertEquals(allValues.get(1).getBalance(),10000);

    }


    @Test
    @DisplayName("해지된 계좌인경우 송금 실패")
    void 송금실패_해지계좌() {
        //given
        // 거래 종류 송금으로 설정
        trans.setTransType(TransType.TRANS);
        // 계좌 상태를 해지 상태로 설정
        targetAccount.setAccStat(AccountStatus.UNREGISTERED);
        // account 조회 모킹
        given(accountRepository.findById(any()))
                .willReturn(Optional.ofNullable(AccountEntity.fromDto(account)));
        // targetAccount 조회 모킹
        given(accountRepository.findByAccNum(any()))
                .willReturn(Optional.ofNullable(AccountEntity.fromDto(targetAccount)));
        //when
        FintechException fintechException = Assertions.assertThrows(FintechException.class,
                () -> transService.createTransaction(trans));
        //then
        // 거래 대상자의 계좌가 해지 계좌이고 해지계좌는 거래가 불가능 하므로 예외가 발생함을 예상
        Assertions.assertEquals(ErrorCode.CAN_NOT_TRANS_UNREGISTERED_ACCOUNT,fintechException.getErrorCode());
    }

    @Test
    @DisplayName("잘못된 거래 종류인 경우 송금 실패")
    void 송금실패_잘못된거래종류() {
        //given
        // 잘못된 거래 종류 설정
        trans.setTransType(TransType.EX);
        // account 조회 모킹
        given(accountRepository.findById(any()))
                .willReturn(Optional.ofNullable(AccountEntity.fromDto(account)));
        //when
        FintechException fintechException = Assertions.assertThrows(FintechException.class,
                () -> transService.createTransaction(trans));
        //then
        // 잘못된 거래 종류 예외가 발생함을 예상
        Assertions.assertEquals(ErrorCode.INVALID_TRANS_TYPE,fintechException.getErrorCode());

    }

    @Test
    @DisplayName("거래대상자의 계좌가 존재하지않는 경우 송금실패")
    void 송금실패_존재하지않는거래대상자의계좌() {
        //given
        // 거래 종류 송금으로 설정
        trans.setTransType(TransType.TRANS);
        // account 조회 모킹
        given(accountRepository.findById(any()))
                .willReturn(Optional.ofNullable(AccountEntity.fromDto(account)));
        // 거래대상자의 계좌가 존재하지 않는경우
        given(accountRepository.findByAccNum(any()))
                .willReturn(Optional.empty());
        //when
        FintechException fintechException = Assertions.assertThrows(FintechException.class,
                () -> transService.createTransaction(trans));
        //then
        // 잘못된 거래 종류 예외가 발생함을 예상
        Assertions.assertEquals(ErrorCode.NOT_FOUND_ACCOUNT,fintechException.getErrorCode());

    }

    @Test
    @DisplayName("입금을 취소하면 입금했던 금액만큼 출금")
    void 입금취소성공() {
        //given
        // 저장되기 전의 값을 찾기 위해 사용 (실제 검증)
        ArgumentCaptor<AccountEntity> captor = ArgumentCaptor.forClass(AccountEntity.class);
        // account 조회 모킹
        given(accountRepository.findById(any()))
                .willReturn(Optional.ofNullable(AccountEntity.fromDto(account)));
        // trans 조회 모킹
        given(transRepository.findById(any()))
                .willReturn(Optional.ofNullable(TransEntity.fromDto(trans)));
        // trans 저장 모킹
        given(transRepository.save(any()))
                .willReturn(TransEntity.fromDto(trans));
        //when
        transService.cancelTransaction(trans);
        //then
        // 저장 되기전 변경된 잔액 정보 확인 (입금되었는지 확인)
        verify(accountRepository,times(1)).save(captor.capture());
        // 잔액이 2만원인 계좌에 1만원을 입금취소했으므로 1만원을 예상
        Assertions.assertEquals(captor.getValue().getBalance(),10000);

    }

    @Test
    @DisplayName("출금을 취소하면 출금했던 금액만큼 입금")
    void 출금취소성공() {
        //given
        // 거래 종류 출금으로 설정
        trans.setTransType(TransType.WITHDRAWAL);
        // 저장되기 전의 값을 찾기 위해 사용 (실제 검증)
        ArgumentCaptor<AccountEntity> captor = ArgumentCaptor.forClass(AccountEntity.class);
        // account 조회 모킹
        given(accountRepository.findById(any()))
                .willReturn(Optional.ofNullable(AccountEntity.fromDto(account)));
        // trans 조회 모킹
        given(transRepository.findById(any()))
                .willReturn(Optional.ofNullable(TransEntity.fromDto(trans)));
        // trans 저장 모킹
        given(transRepository.save(any()))
                .willReturn(TransEntity.fromDto(trans));
        //when
        transService.cancelTransaction(trans);
        //then
        // 저장 되기전 변경된 잔액 정보 확인 (입금되었는지 확인)
        verify(accountRepository,times(1)).save(captor.capture());
        // 잔액이 2만원인 계좌에 1만원을 출금취소했으므로 3만원을 예상
        Assertions.assertEquals(captor.getValue().getBalance(),30000);

    }
    @Test
    @DisplayName("송금을 취소하면 송금했던 금액만큼 각각 계좌에 입금 및 출금")
    void 송금취소성공() {
        //given
        // 거래 종류 송금으로 설정
        trans.setTransType(TransType.TRANS);
        // 저장되기 전의 값을 찾기 위해 사용 (실제 검증)
        ArgumentCaptor<AccountEntity> captor = ArgumentCaptor.forClass(AccountEntity.class);
        // account 조회 모킹
        given(accountRepository.findById(any()))
                .willReturn(Optional.ofNullable(AccountEntity.fromDto(account)));
        // trans 조회 모킹
        given(transRepository.findById(any()))
                .willReturn(Optional.ofNullable(TransEntity.fromDto(trans)));
        // trans 저장 모킹
        given(transRepository.save(any()))
                .willReturn(TransEntity.fromDto(trans));
        // targetAccount 조회 모킹
        given(accountRepository.findByAccNum(any()))
                .willReturn(Optional.ofNullable(AccountEntity.fromDto(targetAccount)));
        //when
        transService.cancelTransaction(trans);
        //then
        // 저장 되기전 변경된 잔액 정보 확인 (송금되었는지 확인)
        verify(accountRepository,times(2)).save(captor.capture());
        // 송금했던 금액은 1만원이고 거래대상자의 계좌 잔액은 10만원이므로 취소 이후 9만원임을 예상
        // 송금 후 남아있는 계좌 잔액은 3만원임을 예상
        List<AccountEntity> allValues = captor.getAllValues();
        Assertions.assertEquals(allValues.get(0).getBalance(),90000);
        Assertions.assertEquals(allValues.get(1).getBalance(),30000);

    }


    @Test
    @DisplayName("거래가 존재하지않는 경우 거래취소 실패")
    void 거래취소실패_존재하지않는거래() {
        //given
        given(transRepository.findById(any()))
                .willReturn(Optional.empty());
        //when
        FintechException fintechException = Assertions.assertThrows(FintechException.class,
                () -> transService.cancelTransaction(trans));
        //then
        // 존재하지않는 거래 예외가 발생함을 예상
        Assertions.assertEquals(ErrorCode.NOT_FOUND_TRANS,fintechException.getErrorCode());
    }

    @Test
    @DisplayName("해당 거래가 이미 취소인 경우 거래취소 실패")
    void 거래취소실패_이미취소인거래() {
        //given
        // 취소인 거래 설정
        trans.setTransStat(TransStatus.CANCEL);
        // account 조회 모킹
        given(accountRepository.findById(any()))
                .willReturn(Optional.ofNullable(AccountEntity.fromDto(account)));
        // trans 조회 모킹
        given(transRepository.findById(any()))
                .willReturn(Optional.ofNullable(TransEntity.fromDto(trans)));
        //when
        FintechException fintechException = Assertions.assertThrows(FintechException.class,
                () -> transService.cancelTransaction(trans));
        //then
        // 거래취소기간 이후 예외가 발생함을 예상
        Assertions.assertEquals(ErrorCode.ALREADY_CANCEL_TRANS,fintechException.getErrorCode());

    }

    @Test
    @DisplayName("거래취소기간 이후인 경우 거래취소 실패")
    void 거래취소실패_거래취소기간이후() {
        //given
        // 해당 거래일시로부터 6일이 지난 시간 설정
        trans.setTransDate(LocalDateTime.now().minusDays(6));
        // account 조회 모킹
        given(accountRepository.findById(any()))
                .willReturn(Optional.ofNullable(AccountEntity.fromDto(account)));
        // trans 조회 모킹
        given(transRepository.findById(any()))
                .willReturn(Optional.ofNullable(TransEntity.fromDto(trans)));
        //when
        FintechException fintechException = Assertions.assertThrows(FintechException.class,
                () -> transService.cancelTransaction(trans));
        //then
        // 거래취소기간 이후 예외가 발생함을 예상
        Assertions.assertEquals(ErrorCode.TOO_OLD_CANCEL,fintechException.getErrorCode());

    }
}
