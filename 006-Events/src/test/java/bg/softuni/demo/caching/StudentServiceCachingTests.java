package bg.softuni.demo.caching;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext
class StudentServiceCachingTests {

    @Autowired
    StudentService studentService;

    @Autowired
    CacheManager cacheManager;

    @Test
    void secondCall_isServedFromCache_untilEvicted() {
        studentService.getAllStudents();
        studentService.getAllStudents();
        assertThat(studentService.getLoads()).isEqualTo(1);

        studentService.refresh();
        assertThat(studentService.getAllStudents()).extracting(Student::name).containsExactly("Pesho", "Anna");
        assertThat(studentService.getLoads()).isEqualTo(2);
    }

    @Test
    void customizer_predefinesCacheNames() {
        assertThat(cacheManager.getCacheNames()).contains("students", "courses");
    }
}
