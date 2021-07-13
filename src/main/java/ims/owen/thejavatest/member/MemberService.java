package ims.owen.thejavatest.member;

import ims.owen.thejavatest.domain.Member;

import java.util.Optional;

public interface MemberService {
    Optional<Member> findById(Long memberId);
}
