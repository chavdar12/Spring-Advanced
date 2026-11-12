package bg.softuni.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BooksControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void getAll_returnsSeededBooks() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Spring in Action"));
    }

    @Test
    void getById_returnsBookOr404() throws Exception {
        mockMvc.perform(get("/api/books/2")).andExpect(status().isOk()).andExpect(jsonPath("$.author").value("Joshua Bloch"));
        mockMvc.perform(get("/api/books/99")).andExpect(status().isNotFound());
    }

    @Test
    void create_returns201WithLocation() throws Exception {
        mockMvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": "Clean Code", "author": "Robert Martin", "isbn": "9780132350884"}"""))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/api/books/3")))
                .andExpect(jsonPath("$.id").value(3));
    }

    @Test
    void create_rejectsInvalidAndDuplicateBooks() throws Exception {
        mockMvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON).content("{\"title\": \"\"}"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": "Copy", "author": "Someone", "isbn": "9780134685991"}"""))
                .andExpect(status().isConflict());
    }

    @Test
    void update_changesBookOr404() throws Exception {
        String body = """
                {"title": "Effective Java 3rd", "author": "Joshua Bloch", "isbn": "9780134685991"}""";
        mockMvc.perform(put("/api/books/2").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Effective Java 3rd"));
        mockMvc.perform(put("/api/books/99").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_removesBookOr404() throws Exception {
        mockMvc.perform(delete("/api/books/1")).andExpect(status().isNoContent());
        mockMvc.perform(get("/api/books/1")).andExpect(status().isNotFound());
        mockMvc.perform(delete("/api/books/1")).andExpect(status().isNotFound());
    }
}
