package ru.otus.hw.dao;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
	private final TestFileNameProvider fileNameProvider;

	@Override
	public List<Question> findAll() {
		try (InputStream inputStream = getClass().getResourceAsStream(fileNameProvider.getTestFileName())) {
			Reader reader = new InputStreamReader(Objects.requireNonNull(inputStream));
			var strategy = new ColumnPositionMappingStrategy<QuestionDto>();
			strategy.setType(QuestionDto.class);
			var csvToBean = new CsvToBeanBuilder<QuestionDto>(reader)
					.withMappingStrategy(strategy)
					.withSeparator(';')
					.withSkipLines(1)
					.withIgnoreLeadingWhiteSpace(true)
					.build();
			return getQuestionsFromQuestionDto(csvToBean.parse());
		} catch (Exception e) {
			throw new QuestionReadException("Error reading the questions file", e);
		}
	}

	private List<Question> getQuestionsFromQuestionDto(List<QuestionDto> questionDtoList) {
		var questions = new ArrayList<Question>();
		questionDtoList.forEach(questionDto -> questions.add(questionDto.toDomainObject()));
		return questions;
	}
}
