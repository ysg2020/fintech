package ysg.fintech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ysg.fintech.entity.MemberEntity;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity,Integer> {
    Optional<MemberEntity> findByName(String name);

}
