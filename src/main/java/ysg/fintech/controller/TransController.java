package ysg.fintech.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ysg.fintech.dto.TransDto;
import ysg.fintech.service.TransService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/trans")
public class TransController {

    private final TransService transService;

    // 거래 내역 조회
    @GetMapping
    public List<TransDto> readTransaction(@RequestParam(name = "accountIdx") @NotNull int accountIdx){
        return transService.readTrans(accountIdx);
    }
    // 거래 (입출금 및 송금)
    @PostMapping
    public TransDto createTransaction(@RequestBody @Validated TransDto transDto){
        return transService.createTransaction(transDto);
    }
    // 거래 취소
    @DeleteMapping
    public TransDto cancelTransaction(@RequestBody @Validated TransDto transDto){
        return transService.cancelTransaction(transDto);
    }

}
