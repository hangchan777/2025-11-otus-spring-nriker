package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.exceptions.UserAnswerReadException;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            printQuestion(question);
            var isAnswerAccepted = new AtomicBoolean(false);
            var isAnswerValid = false;
            while (!isAnswerAccepted.get()) {
                try {
                    var userAnswer = ioService.readStringWithPrompt("Your answer?");
                    isAnswerValid = checkAnswer(question.answers(), userAnswer);
                    isAnswerAccepted.set(true);
                } catch (UserAnswerReadException e) {
                    ioService.printFormattedLine("Your answer does not match the answer options. Please, try again");
                }
            }
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private void printQuestion(Question question) {
        ioService.printFormattedLine(question.text());
        AtomicInteger questionNumber = new AtomicInteger(1);
        question.answers().forEach(answer -> {
            ioService.printFormattedLine(questionNumber.toString() + ". " + answer.text());
            questionNumber.getAndIncrement();
        });
    }

    private boolean checkAnswer(List<Answer> answers, String userAnswer) {
        var answerResult = answers.stream().filter(answer -> answer.text().equals(userAnswer)).findFirst();
        if (answerResult.isPresent()) {
            return answerResult.get().isCorrect();
        }
        throw new UserAnswerReadException("An inappropriate response was entered");
    }
}
