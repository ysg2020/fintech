package ysg.fintech.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ysg.fintech.Repository.AccountRepository;
import ysg.fintech.Repository.MemberRepository;
import ysg.fintech.Repository.TransRepository;
import ysg.fintech.dto.AccountDto;
import ysg.fintech.dto.TransDto;
import ysg.fintech.entity.AccountEntity;
import ysg.fintech.entity.MemberEntity;
import ysg.fintech.entity.TransEntity;
import ysg.fintech.exception.impl.FintechException;
import ysg.fintech.type.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransService {

    private final TransRepository transRepository;
    private final AccountRepository accountRepository;

    // 거래 생성
    @Transactional
    public TransDto createTransaction(TransDto transDto){
        log.info("[TransService] createTransaction start!!");
        // dto > entity 로 변환
        TransEntity trans = TransEntity.fromDto(transDto);
        AccountEntity account = accountRepository.findById(transDto.getAccountIdx().getAccountIdx())
                .orElseThrow(()-> new FintechException(ErrorCode.NOT_FOUND_ACCOUNT));
        // 거래 가능 여부 검증
        validateCreateTransaction(account);
        // 계좌 잔액 갱신 로직
        // 입금일경우
        if(transDto.getTransType().equals(TransType.DEPOSIT)){
            log.info("DEPOSIT start!!");
            account.deposit(transDto.getAmount());
        }
        // 출금일경우
        else if(transDto.getTransType().equals(TransType.WITHDRAWAL)){
            log.info("WITHDRAWAL start!!");
            account.withdrawal(transDto.getAmount());
        }
        // 송금일경우
        else if(transDto.getTransType().equals(TransType.TRANS)){
            log.info("TRANS start!!");
            AccountEntity targetAccount = accountRepository.findByAccNum(transDto.getTransTargetAccNum())
                    .orElseThrow(()-> new FintechException(ErrorCode.NOT_FOUND_ACCOUNT));
            // 거래 가능 여부 검증
            validateCreateTransaction(targetAccount);
            // 내 계좌 출금
            account.withdrawal(transDto.getAmount());
            // 대상 계좌 입금
            targetAccount.deposit(transDto.getAmount());
            // 대상 계좌 변경된 잔액 정보 저장
            accountRepository.save(targetAccount);
        }
        // 잘못된 거래 종류일경우
        else{
            log.info("TransType error!");
            throw new FintechException(ErrorCode.INVALID_TRANS_TYPE);
        }
        // 변경된 잔액 정보 저장
        accountRepository.save(account);

        // entity > dto 로 변환 후 리턴
        return TransDto.fromEntity(transRepository.save(trans));

    }

    // 거래 취소
    @Transactional
    public TransDto cancelTransaction(TransDto transDto){
        log.info("[TransService] cancelTransaction start!!");
        TransEntity trans = transRepository.findById(transDto.getTransIdx())
                .orElseThrow(()-> new FintechException(ErrorCode.NOT_FOUND_TRANS));

        AccountEntity account = accountRepository.findById(transDto.getAccountIdx().getAccountIdx())
                .orElseThrow(()-> new FintechException(ErrorCode.NOT_FOUND_ACCOUNT));
        // 거래 가능 여부 검증
        validateCreateTransaction(account);
        // 거래 취소 가능 여부 검증
        validateCancelTransaction(trans);
        // 계좌 잔액 갱신 로직
        // 입금일경우 출금
        if(transDto.getTransType().equals(TransType.DEPOSIT)){
            log.info("DEPOSIT start!!");
            account.withdrawal(transDto.getAmount());
        }
        // 출금일경우 입금
        else if(transDto.getTransType().equals(TransType.WITHDRAWAL)){
            log.info("WITHDRAWAL start!!");
            account.deposit(transDto.getAmount());
        }
        // 송금일경우
        else if(transDto.getTransType().equals(TransType.TRANS)){
            log.info("TRANS start!!");
            AccountEntity targetAccount = accountRepository.findByAccNum(transDto.getTransTargetAccNum())
                    .orElseThrow(()-> new FintechException(ErrorCode.NOT_FOUND_ACCOUNT));
            // 거래 가능 여부 검증
            validateCreateTransaction(targetAccount);
            // 내 계좌 입금
            account.deposit(transDto.getAmount());
            // 대상 계좌 출금
            targetAccount.withdrawal(transDto.getAmount());
            // 대상 계좌 변경된 잔액 정보 저장
            accountRepository.save(targetAccount);
        }
        // 잘못된 거래 종류일경우
        else{
            log.info("TransType error!");
            throw new FintechException(ErrorCode.INVALID_TRANS_TYPE);
        }
        // 변경된 잔액 정보 저장
        accountRepository.save(account);

        // 거래 취소 설정
        trans.cancelTrans();
        // entity > dto 로 변환 후 리턴
        return TransDto.fromEntity(transRepository.save(trans));

    }

    // 거래내역 조회
    public List<TransDto> readTrans(TransDto transDto){
        return transRepository.findByAccountIdx(transDto.getAccountIdx()).stream()
                .map(TransDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 거래 가능 여부 검증
    private void validateCreateTransaction(AccountEntity account){
        // 해지가 되어 있는 계좌일경우
        if(account.getAccStat().equals(AccountStatus.UNREGISTERED)){
            throw new FintechException(ErrorCode.CAN_NOT_TRANS_UNREGISTERED_ACCOUNT);
        }
    }

    // 거래 취소 가능 여부 검증
    private void validateCancelTransaction(TransEntity trans){
        // 이미 취소한 거래인 경우
        if(trans.getTransStat().equals(TransStatus.CANCEL)){
            throw new FintechException(ErrorCode.ALREADY_CANCEL_TRANS);
        }
        // 거래일시로부터 특정기간이 지난 경우
        if(trans.getTransDate().plusDays(FintechConstants.TERM).isBefore(LocalDateTime.now())){
            throw new FintechException(ErrorCode.TOO_OLD_CANCEL);
        }
    }
}
