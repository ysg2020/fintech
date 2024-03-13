package ysg.fintech.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ysg.fintech.Repository.AccountRepository;
import ysg.fintech.Repository.TransRepository;
import ysg.fintech.dto.TransDto;
import ysg.fintech.entity.AccountEntity;
import ysg.fintech.entity.TransEntity;
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
                .orElse(new AccountEntity());
        // 계좌 잔액 갱신 로직
        // 입금일경우
        if(transDto.getTransType().equals("DEPOSIT")){
            log.info("DEPOSIT start!!");
            account.deposit(transDto.getAmount());
        }
        // 출금일경우
        else if(transDto.getTransType().equals("WITHDRAWAL")){
            log.info("WITHDRAWAL start!!");
            account.withdrawal(transDto.getAmount());
        }
        // 송금일경우
        else{
            log.info("TRANS start!!");
            AccountEntity target = accountRepository.findByName(transDto.getTransTarget())
                    .orElse(new AccountEntity());
            // 내 계좌 출금
            account.withdrawal(transDto.getAmount());
            // 대상 계좌 입금
            target.deposit(transDto.getAmount());
            // 대상 계좌 변경된 잔액 정보 저장
            accountRepository.save(target);
        }
        // 변경된 잔액 정보 저장
        accountRepository.save(account);

        // entity > dto 로 변환 후 리턴
        return TransDto.fromEntity(transRepository.save(trans));

    }
}
