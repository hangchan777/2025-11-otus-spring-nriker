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
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

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
        ioService.printFormattedLine("The test contains %s questions", questionsCount);
        ioService.printFormattedLine("For successful completion, you need to answer %s of them correctly",
                testConfig.getRightAnswersCountToPass());
        ioService.printFormattedLine("Please, answer the questions below%n");
    }

    private TestResult studentTesting(List<Question> questions, Student student) {
        var testResult = new TestResult(student);
        for (var question: questions) {
            printQuestionAndAnswerOptions(question);
            var studentAnswer = getStudentAnswer();
            var isAnswerValid = checkStudentAnswer(question.answers(), studentAnswer);
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private void printQuestionAndAnswerOptions(Question question) {
        ioService.printFormattedLine("Question: %s", question.text());
        ioService.printLine("Answer options:");
        AtomicInteger questionNumber = new AtomicInteger(1);
        question.answers().forEach(answer -> {
            ioService.printFormattedLine(questionNumber + ". " + answer.text());
            questionNumber.getAndIncrement();
        });
        ioService.printLine("");
    }

    private boolean checkStudentAnswer(List<Answer> answers, int studentAnswer) {
        var answerNumber = new AtomicInteger(1);
        for (Answer answer : answers) {
            if (answerNumber.get() == studentAnswer) {
                ioService.printLine("Your answer is accepted");
                ioService.printLine("");
                return answer.isCorrect();
            }
            answerNumber.getAndIncrement();
        }
        return false;
    }

    private int getStudentAnswer() {
        return ioService.readIntForRangeWithPrompt(1, 3,
                "Please, enter your answer:",
                "Your answer does not match the answer options. Please, try again:");
    }
}
