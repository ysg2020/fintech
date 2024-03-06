package ysg.fintech.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ysg.fintech.entity.AccountEntity;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity,Integer> {

    Optional<AccountEntity> findByName(String name);

}
