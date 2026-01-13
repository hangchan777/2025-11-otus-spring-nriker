package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent(value = "Student Test Command")
@RequiredArgsConstructor
public class StudentTestShellCommand {

    private final TestRunnerService testRunnerService;

    @ShellMethod(value = "Start test", key = {"s", "start"})
    public void start() {
        testRunnerService.run();
    }
}
