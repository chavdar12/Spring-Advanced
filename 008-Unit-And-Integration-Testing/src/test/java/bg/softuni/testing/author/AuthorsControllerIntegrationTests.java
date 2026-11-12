package bg.softuni.testing.author;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Integration test: the whole application with a real (in-memory) database.
@SpringBootTest
@AutoConfigureMockMvc
class AuthorsControllerIntegrationTests {

    private static final String AUTHOR_1_NAME = "Ivan Vazov";
    private static final String AUTHOR_2_NAME = "Elin Pelin";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AuthorRepository authorRepository;

    private Long author1Id;

    @BeforeEach
    void setUp() {
        author1Id = authorRepository.save(new AuthorEntity(AUTHOR_1_NAME)).getId();
        authorRepository.save(new AuthorEntity(AUTHOR_2_NAME));
    }

    @AfterEach
    void tearDown() {
        authorRepository.deleteAll();
    }

    @Test
    void getAuthors_returnsAllAuthors() throws Exception {
        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].name", is(AUTHOR_1_NAME)))
                .andExpect(jsonPath("$.[1].name", is(AUTHOR_2_NAME)));
    }

    @Test
    void getAuthor_returnsAuthorOr404() throws Exception {
        mockMvc.perform(get("/authors/{id}", author1Id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(AUTHOR_1_NAME)));
        mockMvc.perform(get("/authors/{id}", -1)).andExpect(status().isNotFound());
    }
}
