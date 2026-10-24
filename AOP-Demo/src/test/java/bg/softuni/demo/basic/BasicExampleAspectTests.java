package bg.softuni.demo.basic;

import bg.softuni.demo.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(properties = "examples.basic.enabled=true")
@ExtendWith(OutputCaptureExtension.class)
class BasicExampleAspectTests {

    @Autowired
    Student student;

    @Test
    void beforeAdvices_runForStudentMethods(CapturedOutput output) {
        student.echo("hi");

        assertThat(output).contains("Before calling: void bg.softuni.demo.Student.echo(String)");
        assertThat(output).contains("Advice execution before calling echo.");
    }

    @Test
    void afterThrowingAdvice_logsAndRethrows(CapturedOutput output) {
        assertThatThrownBy(student::boom).isInstanceOf(IllegalStateException.class);

        assertThat(output).contains("I have detected an exception");
    }
}
