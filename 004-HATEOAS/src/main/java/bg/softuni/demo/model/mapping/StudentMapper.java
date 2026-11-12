package bg.softuni.demo.model.mapping;

import bg.softuni.demo.model.dto.StudentDTO;
import bg.softuni.demo.model.entity.StudentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    StudentDTO mapEntityToDTO(StudentEntity student);
}
