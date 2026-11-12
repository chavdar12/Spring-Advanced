package bg.softuni.demo.caching;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StudentsController {

    private final StudentService studentService;

    public StudentsController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/students")
    public List<Student> students() {
        return studentService.getAllStudents();
    }

    @PostMapping("/students/refresh")
    public void refresh() {
        studentService.refresh();
    }
}
