package ysg.fintech.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ysg.fintech.Repository.AccountRepository;
import ysg.fintech.Repository.MemberRepository;
import ysg.fintech.dto.AccountDto;
import ysg.fintech.entity.AccountEntity;
import ysg.fintech.entity.MemberEntity;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    // 계좌 개설
    public AccountDto createAccount(AccountDto accountDto){
        // dto > entity 로 변환
        AccountEntity account = AccountEntity.fromDto(accountDto);
        // entity > dto 로 변환 후 리턴
        return AccountDto.fromEntity(accountRepository.save(account));
    }

    // 계좌 해지
    public AccountDto dropAccount(AccountDto.DropAccountDto dropAccountDto){
        AccountEntity account = accountRepository.findById(dropAccountDto.getAccountIdx())
                .orElse(new AccountEntity());
        // 계좌 해지 설정
        account.dropAccount(dropAccountDto);
        // entity > dto 로 변환 후 리턴
        return AccountDto.fromEntity(accountRepository.save(account));
    }




}
