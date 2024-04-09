package ysg.fintech.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ysg.fintech.repository.AccountRepository;
import ysg.fintech.dto.AccountDto;
import ysg.fintech.entity.AccountEntity;
import ysg.fintech.entity.MemberEntity;
import ysg.fintech.exception.impl.FintechException;
import ysg.fintech.type.AccountStatus;
import ysg.fintech.type.ErrorCode;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    // 계좌 개설
    public AccountDto createAccount(AccountDto accountDto){
        // dto > entity 로 변환
        AccountEntity account = AccountEntity.fromDto(accountDto);
        // 계좌 개설 가능 여부 검증
        validateCreateAccount(account);
        // entity > dto 로 변환 후 리턴
        return AccountDto.fromEntity(accountRepository.save(account));
    }

    // 계좌 해지
    public AccountDto dropAccount(AccountDto accountDto){
        AccountEntity account = accountRepository.findById(accountDto.getAccountIdx())
                .orElseThrow(()-> new FintechException(ErrorCode.NOT_FOUND_ACCOUNT));
        // 계좌 해지 가능 여부 검증
        validateDropAccount(account);
        // 계좌 해지 설정
        account.dropAccount(accountDto);
        // entity > dto 로 변환 후 리턴
        return AccountDto.fromEntity(accountRepository.save(account));
    }

    // 계좌 목록 조회
    public List<AccountDto> readAccountList(int memberIdx){
        log.info("[Service] readAccountList :{}",memberIdx);
        return accountRepository.findByMemberIdx(MemberEntity.builder()
                        .memberIdx(memberIdx)
                        .build()).stream()
                .map(AccountDto::fromEntity)
                .collect(Collectors.toList());
    }
    // 계좌 조회
    public AccountDto readAccount(String accNum) {
        log.info("[Service] readAccount :{}",accNum);
        return AccountDto.fromEntity(accountRepository.findByAccNum(accNum)
                .orElseThrow(() -> new FintechException(ErrorCode.NOT_FOUND_ACCOUNT)));
    }
    // 계좌 개설 가능 여부 검증
    private void validateCreateAccount(AccountEntity account){
        // 사용자가 존재하지않는 경우
        if (accountRepository.findByMemberIdx(account.getMemberIdx()).isEmpty()) {
            throw new FintechException(ErrorCode.NOT_FOUND_MEMBER);
        }
        // 10개 이상 계좌 개설할 경우
        if (accountRepository.countByMemberIdx(account.getMemberIdx()) >= 10) {
            throw new FintechException(ErrorCode.MAX_CREATE_ACCOUNT);
        }
    }

    // 계좌 해지 가능 여부 검증
    private void validateDropAccount(AccountEntity account){
        // 이미 해지가 되어 있는 계좌일경우
        if (account.getAccStat().equals(AccountStatus.UNREGISTERED)) {
            throw new FintechException(ErrorCode.ALREADY_UNREGISTERED_ACCOUNT);
        }
        // 계좌에 잔액이 남아있는경우
        if (account.getBalance() > 0) {
            throw new FintechException(ErrorCode.NOT_EMPTY_BALANCE);
        }
    }


}
