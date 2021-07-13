package ims.owen.thejavatest.study;

import ims.owen.thejavatest.domain.Member;
import ims.owen.thejavatest.domain.Study;
import ims.owen.thejavatest.member.MemberNotFoundException;
import ims.owen.thejavatest.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

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

    @Test
    void checkMock() throws MemberNotFoundException {
        StudyService studyService = new StudyService(memberService, studyRepository);
        Study study = new Study(12, "테스트");
        Member member = new Member();
        member.setId(1L);
        member.setEmail("Hyeon@naver.com");

//        when(memberService.findById(member.getId())).thenReturn(Optional.of(member));
//        when(studyRepository.save(study)).thenReturn(study);
        //BDD 스타일
        given(memberService.findById(1L)).willReturn(Optional.of(member));
        given(studyRepository.save(study)).willReturn(study);

        studyService.createNewStudy(1L, study);
//        verify(memberService, times(1)).notify(study);
        then(memberService).should(times(1)).notify(study);
//        verifyNoMoreInteractions(memberService);
//        verify(memberService, times(1)).notify(Optional.of(member));
        then(memberService).should(times(1)).notify(Optional.of(member));
        verify(memberService, never()).validate(); // -> 이거는 BDD 스타일로 뭔지 찾아보기
        InOrder inOrder = inOrder(memberService);
        inOrder.verify(memberService).notify(study);
        inOrder.verify(memberService).notify(Optional.of(member));

    }
    @Test
    @DisplayName("Mockito 연습문제 : 다른 사용자가 볼 수 있도록 스터디를 공개한다.")
    void mockitoTestQuestion() {
        //given
        StudyService studyService = new StudyService(memberService, studyRepository);
        Study study = new Study(10, "더 자바, 테스트");
        //TODO studyRepository Mock 객체의 save 메소드를 호출 시 study 를 리턴하도록 만들기
        when(studyRepository.save(study)).thenReturn(study);

        //when
        studyService.openStudy(study);

        //then
        //TODO study의 status가 OPEND로 변경됐는지 확인
        Assertions.assertThat(study.getStatus()).isEqualTo(StudyStatus.OPEND);
        //TODO study의 opendDataTime이 null이 아닌지 확인
        Assertions.assertThat(study.getOpendDataTime()).isNotNull();
        System.out.println("studyGetOpendDataTime = " + study.getOpendDataTime());
        //TODO memberService의 notify(study)가 호출 됐는지 확인
        verify(memberService,times(1)).notify(study);


    }
}