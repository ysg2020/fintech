package ysg.fintech.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ysg.fintech.entity.TransEntity;

public interface TransRepository extends JpaRepository<TransEntity,Integer> {

}
