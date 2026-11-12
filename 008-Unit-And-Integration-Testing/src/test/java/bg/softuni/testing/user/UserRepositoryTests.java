package bg.softuni.testing.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

// Slice test: only JPA (entities, repositories, an in-memory database); each test runs in a rolled-back transaction.
@DataJpaTest
class UserRepositoryTests {

    @Autowired
    UserRepository userRepository;

    @Test
    void findByUsername_findsOnlyTheMatchingUser() {
        userRepository.save(new UserEntity().setUsername("Pesho").setPassword("123"));
        userRepository.save(new UserEntity().setUsername("Anna").setPassword("456"));

        assertThat(userRepository.findByUsername("Anna")).get().extracting(UserEntity::getPassword).isEqualTo("456");
        assertThat(userRepository.findByUsername("Gosho")).isEmpty();
    }
}
