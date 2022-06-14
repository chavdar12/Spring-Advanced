package bg.softuni.demo.web;

import bg.softuni.demo.model.dto.OrderDTO;
import bg.softuni.demo.model.dto.StudentDTO;
import bg.softuni.demo.model.entity.OrderEntity;
import bg.softuni.demo.model.entity.StudentEntity;
import bg.softuni.demo.model.mapping.StudentMapper;
import bg.softuni.demo.repository.StudentsRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequestMapping("/students")
@RestController
public class StudentsController {

    private final StudentsRepository studentsRepository;
    private final StudentMapper studentMapper;

    public StudentsController(StudentsRepository studentsRepository, StudentMapper studentMapper) {
        this.studentsRepository = studentsRepository;
        this.studentMapper = studentMapper;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<StudentDTO>>> getStudents() {
        List<EntityModel<StudentDTO>> allStudents = studentsRepository
                .findAll()
                .stream()
                .map(studentMapper::mapEntityToDTO)
                .map(dto -> EntityModel.of(dto, createStudentLinks(dto)))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(allStudents));
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<CollectionModel<EntityModel<OrderDTO>>> getOrders(@PathVariable("id") Long id) {
        StudentEntity student = studentsRepository.findById(id).orElseThrow();

        List<EntityModel<OrderDTO>> orders = student
                .getOrders()
                .stream()
                .map(this::map)
                .map(EntityModel::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(orders));
    }

    private OrderDTO map(OrderEntity order) {
        return new OrderDTO()
                .setId(order.getId())
                .setStudentId(order.getStudent().getId())
                .setCourseId(order.getCourse().getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<StudentDTO>> getStudentsByID(@PathVariable("id") Long id) {
        StudentDTO student = studentsRepository
                .findById(id)
                .map(studentMapper::mapEntityToDTO)
                .orElseThrow();

        return ResponseEntity.ok(EntityModel.of(student, createStudentLinks(student)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<StudentDTO>> update(@PathVariable Long id,
                                                          StudentDTO studentDTO) {
        return ResponseEntity.ok().build();
    }

    private Link[] createStudentLinks(StudentDTO studentDTO) {
        List<Link> result = new ArrayList<>();

        Link selfLink = linkTo(methodOn(StudentsController.class).
                getStudentsByID(studentDTO.getId())).withSelfRel();
        result.add(selfLink);

        Link updateLink = linkTo(methodOn(StudentsController.class)
                .update(studentDTO.getId(), studentDTO)).withRel("update");
        result.add(updateLink);

        Link orderLink = linkTo(methodOn(StudentsController.class)
                .getOrders(studentDTO.getId())).withRel("orders");
        result.add(orderLink);

        return result.toArray(new Link[0]);
    }
}