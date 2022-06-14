package bg.softuni.demo.basic;

import bg.softuni.demo.Student;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "examples.basic.enabled", havingValue = "true")
public class BasicExample implements CommandLineRunner {

    private final Student student;

    public BasicExample(Student student) {
        this.student = student;
    }

    @Override
    public void run(String... args) {
        student.sayHello();
        student.echo("ARGUMENT!");
        student.concat("A", "B");
        try {
            student.boom();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
