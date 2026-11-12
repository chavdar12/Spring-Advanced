package bg.softuni.testing.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

// Unit test: no Spring context, the repository is replaced by a Mockito mock.
@ExtendWith(MockitoExtension.class)
class UserServiceImplTests {

    @Mock
    UserRepository mockedUserRepository;

    UserServiceImpl userService;

    UserEntity testUser;

    @BeforeEach
    void init() {
        testUser = new UserEntity().setId(1L).setUsername("Pesho").setPassword("123");
        userService = new UserServiceImpl(mockedUserRepository);
    }

    @Test
    void getUserWithCorrectUsername_returnsTheUser() {
        // Arrange
        when(mockedUserRepository.findByUsername("Pesho")).thenReturn(Optional.of(testUser));

        // Act
        UserEntity actual = userService.getUserByUsername("Pesho");

        // Assert
        assertThat(actual.getId()).isEqualTo(testUser.getId());
        assertThat(actual.getUsername()).isEqualTo(testUser.getUsername());
        assertThat(actual.getPassword()).isEqualTo(testUser.getPassword());
    }

    @Test
    void getUserWithUnknownUsername_throws() {
        when(mockedUserRepository.findByUsername("Gosho")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserByUsername("Gosho"))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Gosho");
    }
}
