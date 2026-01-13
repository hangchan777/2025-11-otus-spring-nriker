package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.TestConfig;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    private final TestConfig testConfig;

    @Override
    public TestResult executeTestFor(Student student) {
        var questions = questionDao.findAll();
        printTestGreeting(questions.size());
        return studentTesting(questions, student);
    }

    private void printTestGreeting(int questionsCount) {
        ioService.printLine("");
        ioService.printFormattedLineLocalized("TestService.answers.count", questionsCount);
        ioService.printFormattedLineLocalized("TestService.right.answers.count.to.pass",
                testConfig.getRightAnswersCountToPass());
        ioService.printLineLocalized("TestService.answer.the.questions");
    }

    private TestResult studentTesting(List<Question> questions, Student student) {
        var testResult = new TestResult(student);
        for (var question: questions) {
            printQuestionAndAnswerOptions(question);
            var studentAnswer = getStudentAnswer(question.answers().size());
            var isAnswerValid = checkStudentAnswer(question.answers(), studentAnswer);
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private void printQuestionAndAnswerOptions(Question question) {
        ioService.printFormattedLineLocalized("TestService.question", question.text());
        ioService.printLineLocalized("TestService.answer.options");
        var questionNumber = 1;
        for (Answer answer : question.answers()) {
            ioService.printFormattedLine("%s. %s", questionNumber, answer.text());
            questionNumber++;
        }
        ioService.printLine("");
    }

    private boolean checkStudentAnswer(List<Answer> answers, int studentAnswer) {
        var answerNumber = 1;
        for (Answer answer : answers) {
            if (answerNumber == studentAnswer) {
                ioService.printLineLocalized("TestService.answer.accepted");
                ioService.printLine("");
                return answer.isCorrect();
            }
            answerNumber++;
        }
        return false;
    }

    private int getStudentAnswer(int answersCount) {
        return ioService.readIntForRangeWithPromptLocalized(1, answersCount,
                "TestService.enter.answer",
                "TestService.enter.answer.error");
    }
}
