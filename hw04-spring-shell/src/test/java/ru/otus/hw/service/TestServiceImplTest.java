package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.utils.TestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class TestServiceImplTest {

	Student student = new Student("FirstName", "LastName");
	@MockitoBean
	LocalizedIOService ioService;
	@Autowired
	TestServiceImpl testService;

	@Test
	void test_executeTestFor_studentTestSucceed() {
		when(ioService.readIntForRangeWithPromptLocalized(anyInt(), anyInt(), anyString(), anyString())).thenReturn(1);
		var expectedTestResult = getExpectedTestResult(student, true);

		var actualTestResult = testService.executeTestFor(student);

		assertEquals(expectedTestResult, actualTestResult);
	}

	@Test
	void test_executeTestFor_studentTestFailed() {
		when(ioService.readIntForRangeWithPromptLocalized(anyInt(), anyInt(), anyString(), anyString())).thenReturn(2);
		var expectedTestResult = getExpectedTestResult(student, false);

		var actualTestResult = testService.executeTestFor(student);

		assertEquals(expectedTestResult, actualTestResult);
	}

	private TestResult getExpectedTestResult(Student student, boolean result) {
		var testResult = new TestResult(student);
		testResult.applyAnswer(TestUtils.getExpectedQuestions().get(0), result);
		return testResult;
	}
}