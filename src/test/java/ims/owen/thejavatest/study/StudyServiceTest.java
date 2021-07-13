package ims.owen.thejavatest.study;

import ims.owen.thejavatest.domain.Member;
import ims.owen.thejavatest.domain.Study;
import ims.owen.thejavatest.member.MemberNotFoundException;
import ims.owen.thejavatest.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

    @Mock
    MemberService memberService;

    @Mock
    StudyRepository studyRepository;

    @Test
    void createStudyService() throws MemberNotFoundException {
//        MemberService memberService = mock(MemberService.class);
//        StudyRepository studyRepository = mock(StudyRepository.class);
        StudyService studyService = new StudyService(memberService, studyRepository);
        Member member = new Member();
        member.setId(1L);
        member.setEmail("jang@naver.com");

        when(memberService.findById(any())).thenReturn( Optional.of(member));
        Study study = new Study(10, "java");
        studyService.createNewStudy(2L, study);

        Assertions.assertThat(studyService).isNotNull();

        when(memberService.findById(any()))
                .thenReturn(Optional.of(member))
                .thenThrow(new RuntimeException())
                .thenReturn(Optional.empty());

        Optional<Member> byId = memberService.findById(1L);
        Assertions.assertThat("jang@naver.com").isEqualTo(byId.get().getEmail());

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () ->{
            memberService.findById(2L);
        });

        Assertions.assertThat(Optional.empty()).isEqualTo(memberService.findById(3L));
    }

    @Test
    void stubbingPractice() throws MemberNotFoundException {
        StudyService studyService = new StudyService(memberService, studyRepository);
        Study study = new Study(12, "테스트");
        Member member = new Member();
        member.setId(1L);
        member.setEmail("Hyeon@naver.com");

        //TODO memberService 객체에 findById 메소드를 1L 값으로 호출하면 member 객체를 리턴하도록 Stubbing
        when(memberService.findById(member.getId())).thenReturn(Optional.of(member));
        Optional<Member> byId = memberService.findById(1L);
        Assertions.assertThat(member.getId()).isEqualTo(byId.get().getId());

        //TODO studyRepository 객체에 save 메소드를 study 객체로 호출하면 study 객체 그대로 리턴하도록 Stubbing
        when(studyRepository.save(study)).thenReturn(study);
        Study save = studyRepository.save(study);
        Assertions.assertThat(study.getLimit()).isEqualTo(12);

        studyService.createNewStudy(1L, study);
        org.junit.jupiter.api.Assertions.assertEquals(member, study.getOwner());
    }
}