package ysg.fintech.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ysg.fintech.entity.MemberEntity;

public interface MemberRepository extends JpaRepository<MemberEntity,Integer> {

}
