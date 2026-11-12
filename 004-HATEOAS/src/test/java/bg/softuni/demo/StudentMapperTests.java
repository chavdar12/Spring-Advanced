package bg.softuni.demo;

import bg.softuni.demo.model.dto.StudentDTO;
import bg.softuni.demo.model.entity.StudentEntity;
import bg.softuni.demo.model.mapping.StudentMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class StudentMapperTests {

    private final StudentMapper mapper = Mappers.getMapper(StudentMapper.class);

    @Test
    void mapsAllFields() {
        StudentDTO dto = mapper.mapEntityToDTO(new StudentEntity().setId(7L).setName("Ivan").setAge(30));

        assertThat(dto.getId()).isEqualTo(7L);
        assertThat(dto.getName()).isEqualTo("Ivan");
        assertThat(dto.getAge()).isEqualTo(30);
    }

    @Test
    void mapsNullToNull() {
        assertThat(mapper.mapEntityToDTO(null)).isNull();
    }
}
