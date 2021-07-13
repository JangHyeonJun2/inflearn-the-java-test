package ims.owen.thejavatest;

import ims.owen.thejavatest.domain.Study;
import ims.owen.thejavatest.study.StudyStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
@ExtendWith(FindSlowTestExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudyTest {
    int value = 1;

    @Test
    @Order(2)
    @DisplayName("스터디 만들기")
    @EnabledIfEnvironmentVariable(named = "TEST_ENV", matches = "LOCAL")
    void create() {
        Study study = new Study(10);
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> new Study(-10));
        assertEquals("limit은 0보다 커야한다.", exception.getMessage());


        assertAll(
                () -> assertNotNull(study),
                () -> assertEquals(StudyStatus.DRAFT, study.getStatus(), () -> "스터디를 처음 만들면 상태값이 DRAFT여야합니다."),
                () -> assertTrue(study.getLimit() > 0, "스터디 최대 참석 가능 인원은 0보다 커야합니다.")
        );

        assertTimeout(Duration.ofMillis(100), () -> {
            new Study(10);
            Thread.sleep(90);
        });

        System.out.println(value++);


    }

    @Test
    @Order(1)
    @DisplayName("조건에 따라 테스트 실행하기")
    void condition() throws InterruptedException {
        Thread.sleep(1005L);
        String test_env = System.getenv("TEST_ENV");
        System.out.println(test_env);
        assumeTrue("LOCAL".equalsIgnoreCase(test_env));

        System.out.println(value++);
    }

    @Test
    @DisplayName("스터디 만들기 다시")
    @EnabledIfEnvironmentVariable(named = "TEST_ENV", matches = "Owen")
    void create1() {
        System.out.println("create1");
    }


    @RepeatedTest(value = 5, name = "{currentRepetition}/{totalRepetitions}")
    @DisplayName("테스트 반복하기")
    void iterTest(RepetitionInfo repetitionInfo) {
        System.out.println("test" + repetitionInfo.getCurrentRepetition() + "/" + repetitionInfo.getTotalRepetitions());
    }

    @ParameterizedTest(name = "{index} {displayName} message = {0}")
//    @ValueSource(strings = {"날씨가", "많이", "더워지고", "있네요."})
    @CsvSource({"10, '자바 스터디'", "20, 스프링"})
    void parameterizedTest(ArgumentsAccessor argumentsAccessor) {
        Study study = new Study(argumentsAccessor.getInteger(0), argumentsAccessor.getString(1));
        System.out.println(study);
    }

//    static class StudConverter extends SimpleArgumentConverter {
//        @Override
//        protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
//            return new Study(Integer.parseInt(source.toString()));
//        }
//    }


    @BeforeAll
    static void beforeAll() {
        System.out.println("before all");
    }
    @AfterAll
    static void afterAll() {
        System.out.println("after all");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("before each");
    }

    @AfterEach
    void afterEach() {
        System.out.println("After each");
    }
}