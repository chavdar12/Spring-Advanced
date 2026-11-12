package bg.softuni.testing.message;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class MessageServiceTests {

    @Autowired
    MessageService messageService;

    @Test
    void getMessage_withoutUser_isRejected() {
        assertThatThrownBy(messageService::getMessage).isInstanceOf(AuthenticationCredentialsNotFoundException.class);
    }

    @Test
    @WithMockUser("customUsername")
    void getMessage_withMockUserCustomUsername() {
        assertThat(messageService.getMessage()).isEqualTo("Hello, customUsername!");
    }

    @Test
    @WithMockUser(username = "pesho", roles = "USER")
    void getAdminMessage_asUser_isDenied() {
        assertThatThrownBy(messageService::getAdminMessage).isInstanceOf(AuthorizationDeniedException.class);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void getAdminMessage_asAdmin() {
        assertThat(messageService.getAdminMessage()).isEqualTo("Hello, admin!");
    }
}
