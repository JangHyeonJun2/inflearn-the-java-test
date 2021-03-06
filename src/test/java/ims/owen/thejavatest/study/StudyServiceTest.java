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
        Study study = new Study(12, "?????????");
        Member member = new Member();
        member.setId(1L);
        member.setEmail("Hyeon@naver.com");

        //TODO memberService ????????? findById ???????????? 1L ????????? ???????????? member ????????? ??????????????? Stubbing
        when(memberService.findById(member.getId())).thenReturn(Optional.of(member));
        Optional<Member> byId = memberService.findById(1L);
        Assertions.assertThat(member.getId()).isEqualTo(byId.get().getId());

        //TODO studyRepository ????????? save ???????????? study ????????? ???????????? study ?????? ????????? ??????????????? Stubbing
        when(studyRepository.save(study)).thenReturn(study);
        Study save = studyRepository.save(study);
        Assertions.assertThat(study.getLimit()).isEqualTo(12);

        studyService.createNewStudy(1L, study);
        org.junit.jupiter.api.Assertions.assertEquals(member, study.getOwner());
    }

    @Test
    void checkMock() throws MemberNotFoundException {
        StudyService studyService = new StudyService(memberService, studyRepository);
        Study study = new Study(12, "?????????");
        Member member = new Member();
        member.setId(1L);
        member.setEmail("Hyeon@naver.com");

//        when(memberService.findById(member.getId())).thenReturn(Optional.of(member));
//        when(studyRepository.save(study)).thenReturn(study);
        //BDD ?????????
        given(memberService.findById(1L)).willReturn(Optional.of(member));
        given(studyRepository.save(study)).willReturn(study);

        studyService.createNewStudy(1L, study);
//        verify(memberService, times(1)).notify(study);
        then(memberService).should(times(1)).notify(study);
//        verifyNoMoreInteractions(memberService);
//        verify(memberService, times(1)).notify(Optional.of(member));
        then(memberService).should(times(1)).notify(Optional.of(member));
        verify(memberService, never()).validate(); // -> ????????? BDD ???????????? ?????? ????????????
        InOrder inOrder = inOrder(memberService);
        inOrder.verify(memberService).notify(study);
        inOrder.verify(memberService).notify(Optional.of(member));

    }
    @Test
    @DisplayName("Mockito ???????????? : ?????? ???????????? ??? ??? ????????? ???????????? ????????????.")
    void mockitoTestQuestion() {
        //given
        StudyService studyService = new StudyService(memberService, studyRepository);
        Study study = new Study(10, "??? ??????, ?????????");
        //TODO studyRepository Mock ????????? save ???????????? ?????? ??? study ??? ??????????????? ?????????
        when(studyRepository.save(study)).thenReturn(study);

        //when
        studyService.openStudy(study);

        //then
        //TODO study??? status??? OPEND??? ??????????????? ??????
        Assertions.assertThat(study.getStatus()).isEqualTo(StudyStatus.OPEND);
        //TODO study??? opendDataTime??? null??? ????????? ??????
        Assertions.assertThat(study.getOpendDataTime()).isNotNull();
        System.out.println("studyGetOpendDataTime = " + study.getOpendDataTime());
        //TODO memberService??? notify(study)??? ?????? ????????? ??????
        verify(memberService,times(1)).notify(study);


    }
}