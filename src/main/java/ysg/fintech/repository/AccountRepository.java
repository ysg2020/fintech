package ysg.fintech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ysg.fintech.entity.AccountEntity;
import ysg.fintech.entity.MemberEntity;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity,Integer> {

    List<AccountEntity> findByMemberIdx(MemberEntity member);
    Optional<AccountEntity> findByAccNum(String accNum);
    Integer countByMemberIdx(MemberEntity member);

}
