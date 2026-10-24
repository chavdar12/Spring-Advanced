package bg.softuni.demo.modifying;

import bg.softuni.demo.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "examples.modifying.enabled=true")
class ModifyingAspectTests {

    @Autowired
    Student student;

    @Test
    void aroundAdvice_modifiesArgumentsAndResult() {
        assertThat(student.concat("A", "B")).isEqualTo("([A][B])");
    }
}
