package ims.owen.thejavatest.study;

import ims.owen.thejavatest.domain.Member;
import ims.owen.thejavatest.domain.Study;
import ims.owen.thejavatest.member.MemberNotFoundException;
import ims.owen.thejavatest.member.MemberService;

import java.util.Optional;

public class StudyService {
    private final MemberService memberService;

    private final StudyRepository repository;

    public StudyService(MemberService memberService, StudyRepository repository) {
        assert memberService != null;
        assert repository != null;
        this.memberService = memberService;
        this.repository = repository;
    }

    public Study createNewStudy(Long memberId, Study study) throws MemberNotFoundException {
        Optional<Member> member = memberService.findById(memberId);
        study.setOwner(member.orElseThrow(() -> new IllegalArgumentException("Member doesn`t exist for id: '" + member)));
        Study newStudy = repository.save(study);
        memberService.notify(newStudy);
//        memberService.notify(member);
        return newStudy;
    }
}
