package ims.owen.thejavatest.member;

import ims.owen.thejavatest.domain.Member;
import ims.owen.thejavatest.domain.Study;

import java.util.Optional;

public interface MemberService {
    Optional<Member> findById(Long memberId);
    void validate();
    void notify(Study newStudy);
    void notify(Optional<Member> member);
}


