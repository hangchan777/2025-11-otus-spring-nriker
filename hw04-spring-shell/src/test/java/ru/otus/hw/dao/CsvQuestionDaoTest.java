package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.utils.TestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CsvQuestionDaoTest {

	@Autowired
	private QuestionDao csvQuestionDao;

	@Test
	void findAll() {
		var expectedQuestions = TestUtils.getExpectedQuestions();

		var actualQuestions =  csvQuestionDao.findAll();

		assertThat(actualQuestions)
				.hasSize(1)
				.usingRecursiveComparison()
				.isEqualTo(expectedQuestions);
	}
}