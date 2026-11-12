package bg.softuni.security;

import bg.softuni.security.user.UserRepository;
import bg.softuni.security.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(OutputCaptureExtension.class)
class SecurityTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void homePage_isPublic_andShowsGuestGreeting() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello, guest!")));
    }

    @Test
    void protectedPage_redirectsAnonymousToLogin() throws Exception {
        mockMvc.perform(get("/profile")).andExpect(status().isFound()).andExpect(redirectedUrl("/login"));
    }

    @Test
    void register_storesEncodedPassword_andAllowsLogin() throws Exception {
        mockMvc.perform(post("/register").with(csrf())
                        .param("username", "pesho").param("password", "secret1").param("confirmPassword", "secret1"))
                .andExpect(redirectedUrl("/login"));

        String stored = userRepository.findByUsername("pesho").orElseThrow().getPassword();
        assertThat(stored).startsWith("{bcrypt}").isNotEqualTo("secret1");
        assertThat(passwordEncoder.matches("secret1", stored)).isTrue();

        mockMvc.perform(formLogin("/login").user("pesho").password("secret1"))
                .andExpect(authenticated().withUsername("pesho").withRoles("USER"));
        mockMvc.perform(formLogin("/login").user("pesho").password("wrong"))
                .andExpect(unauthenticated())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    void register_rejectsInvalidInputAndTakenUsernames() throws Exception {
        mockMvc.perform(post("/register").with(csrf())
                        .param("username", "ivan").param("password", "secret1").param("confirmPassword", "other"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Passwords do not match.")));

        mockMvc.perform(post("/register").with(csrf())
                        .param("username", "admin").param("password", "secret1").param("confirmPassword", "secret1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("This username is already taken.")));
    }

    @Test
    void postWithoutCsrfToken_isForbidden() throws Exception {
        mockMvc.perform(post("/register").param("username", "x").param("password", "y").param("confirmPassword", "y"))
                .andExpect(status().isForbidden());
    }

    @Test
    void rememberMe_setsCookie() throws Exception {
        mockMvc.perform(post("/login").with(csrf()).param("username", "admin").param("password", "admin12345").param("remember", "on"))
                .andExpect(cookie().exists("remember-me"));
    }

    @Test
    @WithMockUser(username = "pesho", roles = "USER")
    void userCannotOpenAdminPages() throws Exception {
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/unauthorized"));
        mockMvc.perform(get("/")).andExpect(content().string(not(containsString("only shown to administrators"))));
    }

    @Test
    @WithMockUser(username = "boss", roles = {"USER", "ADMIN"})
    void adminSeesAdminContent() throws Exception {
        mockMvc.perform(get("/admin/users")).andExpect(status().isOk()).andExpect(content().string(containsString("admin")));
        mockMvc.perform(get("/")).andExpect(content().string(containsString("only shown to administrators")));
    }

    @Test
    @WithMockUser(roles = "USER")
    void methodSecurity_blocksNonAdmins() {
        assertThatThrownBy(() -> userService.delete("admin")).isInstanceOf(AuthorizationDeniedException.class);
        assertThat(userRepository.existsByUsername("admin")).isTrue();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void methodSecurity_allowsAdmins() {
        userService.delete("admin");
        assertThat(userRepository.existsByUsername("admin")).isFalse();
    }

    @Test
    void filterAndInterceptor_runForEveryRequest(CapturedOutput output) throws Exception {
        mockMvc.perform(get("/")).andExpect(header().string("X-Greeting", "Hello from the filter"));

        assertThat(output).contains("Handling GET /").contains("Completed GET / with status 200");
    }
}
