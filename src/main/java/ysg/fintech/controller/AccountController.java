package ysg.fintech.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ysg.fintech.dto.AccountDto;
import ysg.fintech.service.AccountService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    // 계좌 목록 조회
    @GetMapping("/list")
    public List<AccountDto> readAccountList(@RequestParam(name = "memberIdx") int memberIdx){
        log.info("[Controller] readAccountList : {}",memberIdx);
        return accountService.readAccountList(memberIdx);
    }
    // 계좌 조회
    @GetMapping
    public AccountDto readAccount(@RequestParam(name = "accNum") String accNum){
        log.info("[Controller] readAccount : {}",accNum);
        return accountService.readAccount(accNum);
    }
    // 계좌 개설
    @PostMapping
    public AccountDto createAccount(@RequestBody AccountDto accountDto){
        return accountService.createAccount(accountDto);
    }
    // 계좌 해지
    @DeleteMapping
    public AccountDto dropAccount(@RequestBody AccountDto accountDto){
        return accountService.dropAccount(accountDto);
    }
}
