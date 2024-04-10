package ysg.fintech.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ysg.fintech.dto.TransDto;
import ysg.fintech.type.TransStatus;
import ysg.fintech.type.TransType;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@SpringBootTest
public class TransConcurrencyTest {


    @Autowired
    private TransService transService;
    @Test
    void 출금동시테스트() throws InterruptedException {
        // 동시성 테스트 시작

        // Given
        // 동시성 테스트 준비
        int numberOfThreads = 2;
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        TransDto request1 = TransDto.builder()
                .accountIdx(1001)
                .memberIdx(2)
                .transType(TransType.WITHDRAWAL)
                .transStat(TransStatus.SUCCESS)
                .amount(10000)
                .transDate(LocalDateTime.now())
                .build();

        TransDto request2 = TransDto.builder()
                .accountIdx(1001)
                .memberIdx(2)
                .transType(TransType.WITHDRAWAL)
                .transStat(TransStatus.SUCCESS)
                .amount(10000)
                .transDate(LocalDateTime.now())
                .build();

        // When
        // 동시성 테스트 진행
        service.execute(() -> {
            transService.createTransaction(request1);
            latch.countDown();
        });
        service.execute(() -> {
            transService.createTransaction(request2);
            latch.countDown();
        });
        latch.await();

    }

    @Test
    void 입금() {
        //given
        TransDto request1 = TransDto.builder()
                .accountIdx(1001)
                .memberIdx(2)
                .transType(TransType.DEPOSIT)
                .transStat(TransStatus.SUCCESS)
                .amount(10000)
                .transDate(LocalDateTime.now())
                .build();
        //when
        transService.createTransaction(request1);
        //then

    }
}
