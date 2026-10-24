package bg.softuni.demo.repository;

import bg.softuni.demo.model.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentsRepository extends JpaRepository<StudentEntity, Long> {

}
