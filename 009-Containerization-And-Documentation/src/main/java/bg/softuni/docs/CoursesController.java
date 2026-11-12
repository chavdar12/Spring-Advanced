package bg.softuni.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Courses", description = "Manage the course catalogue")
@RestController
@RequestMapping("/api/courses")
public class CoursesController {

    private final CourseRepository courseRepository;

    public CoursesController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Operation(summary = "List all courses")
    @GetMapping
    public List<CourseDTO> getAll() {
        return courseRepository.findAll().stream().map(CourseDTO::of).toList();
    }

    @Operation(summary = "Get a course by id")
    @ApiResponse(responseCode = "200", description = "The course")
    @ApiResponse(responseCode = "404", description = "No course with this id")
    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.of(courseRepository.findById(id).map(CourseDTO::of));
    }

    @Operation(summary = "Create a course")
    @ApiResponse(responseCode = "201", description = "Created")
    @ApiResponse(responseCode = "400", description = "Invalid course")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseDTO create(@Valid @RequestBody CourseDTO course) {
        return CourseDTO.of(courseRepository.save(new CourseEntity(course.name(), course.price())));
    }
}
