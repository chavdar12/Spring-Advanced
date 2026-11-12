package bg.softuni.testing.author;

import bg.softuni.testing.SecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Slice test: only the web layer is started; the repository is a mock, so no database is involved.
@WebMvcTest(AuthorsController.class)
@Import(SecurityConfiguration.class)
class AuthorsControllerWebMvcTests {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AuthorRepository authorRepository;

    @Test
    void getAuthors_mapsRepositoryResultToJson() throws Exception {
        when(authorRepository.findAll()).thenReturn(List.of(new AuthorEntity("Mocked Author")));

        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Mocked Author"));
    }
}
