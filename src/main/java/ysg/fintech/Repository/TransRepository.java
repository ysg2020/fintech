package ysg.fintech.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ysg.fintech.entity.AccountEntity;
import ysg.fintech.entity.MemberEntity;
import ysg.fintech.entity.TransEntity;

import java.util.List;

public interface TransRepository extends JpaRepository<TransEntity,Integer> {

    List<TransEntity> findByAccountIdx(AccountEntity account);
}
