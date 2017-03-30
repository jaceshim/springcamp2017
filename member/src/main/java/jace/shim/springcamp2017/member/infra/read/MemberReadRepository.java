package jace.shim.springcamp2017.member.infra.read;

import jace.shim.springcamp2017.member.model.read.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by jaceshim on 2017. 3. 29..
 */
@Repository
public interface MemberReadRepository extends JpaRepository<Member, String> {
}
