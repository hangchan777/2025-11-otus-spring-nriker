package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.TestConfig;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.utils.TestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestServiceImplTest {

	Student student = new Student("FirstName", "LastName");
	@Mock
	IOService ioService;
	@Mock
	QuestionDao questionDao;
	@Mock
	TestConfig testConfig;
	@InjectMocks
	TestServiceImpl testService;

	@Test
	void test_executeTestFor_studentTestSucceed() {
		when(questionDao.findAll()).thenReturn(TestUtils.getExpectedQuestions());
		when(testConfig.getRightAnswersCountToPass()).thenReturn(3);
		when(ioService.readIntForRangeWithPrompt(anyInt(), anyInt(), anyString(), anyString())).thenReturn(1);
		var expectedTestResult = getExpectedTestResult(student, true);

		var actualTestResult = testService.executeTestFor(student);

		assertEquals(expectedTestResult, actualTestResult);
		verify(questionDao, times(1)).findAll();
		verify(ioService, times(5)).printLine(anyString());
		verify(ioService, times(1)).printFormattedLine(anyString());
		verify(ioService, times(1)).printFormattedLine(anyString(), anyString());
		verify(ioService, times(3)).printFormattedLine(anyString(), any(), anyString());
		verify(ioService, times(1)).readIntForRangeWithPrompt(anyInt(), anyInt(),
				anyString(), anyString());
	}

	@Test
	void test_executeTestFor_studentTestFailed() {
		when(questionDao.findAll()).thenReturn(TestUtils.getExpectedQuestions());
		when(testConfig.getRightAnswersCountToPass()).thenReturn(3);
		when(ioService.readIntForRangeWithPrompt(anyInt(), anyInt(), anyString(), anyString())).thenReturn(2);
		var expectedTestResult = getExpectedTestResult(student, false);

		var actualTestResult = testService.executeTestFor(student);

		assertEquals(expectedTestResult, actualTestResult);
		verify(questionDao, times(1)).findAll();
		verify(ioService, times(5)).printLine(anyString());
		verify(ioService, times(1)).printFormattedLine(anyString());
		verify(ioService, times(1)).printFormattedLine(anyString(), anyString());
		verify(ioService, times(3)).printFormattedLine(anyString(), any(), anyString());
		verify(ioService, times(1)).readIntForRangeWithPrompt(anyInt(), anyInt(),
				anyString(), anyString());
	}

	private TestResult getExpectedTestResult(Student student, boolean result) {
		var testResult = new TestResult(student);
		testResult.applyAnswer(TestUtils.getExpectedQuestions().get(0), result);
		return testResult;
	}
}