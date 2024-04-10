package ysg.fintech.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ysg.fintech.entity.AccountEntity;
import ysg.fintech.entity.MemberEntity;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity,Integer> {

    List<AccountEntity> findByMemberIdx(MemberEntity member);
    Optional<AccountEntity> findByAccNum(String accNum);
    Integer countByMemberIdx(MemberEntity member);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from account a where a.accountIdx = :accountIdx")
    Optional<AccountEntity> findByIdWithLock(@Param("accountIdx") int accountIdx);

}
