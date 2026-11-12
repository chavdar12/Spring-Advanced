package bg.softuni.books;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BooksControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void listAllBooks_includesTags() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].tags", contains("fantasy", "magic")));
    }

    @Test
    void createBook_withTags() throws Exception {
        mockMvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": "Dune", "author": "Frank Herbert", "isbn": "9780441013593", "tags": ["sci-fi", "classic", "sci-fi"]}"""))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.tags", contains("sci-fi", "classic")));
    }

    @Test
    void editBook_replacesFieldsAndTags() throws Exception {
        mockMvc.perform(put("/api/books/1").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": "Harry Potter 1", "author": "J. K. Rowling", "isbn": "0-7475-3269-9", "tags": ["kids"]}"""))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/books/1"))
                .andExpect(jsonPath("$.title").value("Harry Potter 1"))
                .andExpect(jsonPath("$.tags", contains("kids")));
    }

    @Test
    void deleteBook() throws Exception {
        mockMvc.perform(delete("/api/books/2")).andExpect(status().isNoContent());
        mockMvc.perform(get("/api/books")).andExpect(jsonPath("$", hasSize(1)));
        mockMvc.perform(delete("/api/books/2")).andExpect(status().isNotFound());
    }

    @Test
    void invalidBook_isRejected() throws Exception {
        mockMvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": "No author", "isbn": "1", "tags": [" "]}"""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void frontEnd_isServed() throws Exception {
        mockMvc.perform(get("/index.html")).andExpect(status().isOk()).andExpect(content().string(containsString("LOAD ALL BOOKS")));
        mockMvc.perform(get("/app.js")).andExpect(status().isOk());
    }
}
