package bg.softuni.docs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DocumentationTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void openApiSpec_describesTheApi() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.title").value("Courses API"))
                .andExpect(jsonPath("$.paths['/api/courses'].get.summary").value("List all courses"))
                .andExpect(jsonPath("$.paths['/api/courses/{id}'].get.responses['404'].description").value("No course with this id"))
                .andExpect(jsonPath("$.components.schemas.CourseDTO.required").isArray());
    }

    @Test
    void swaggerUi_isAvailable() throws Exception {
        mockMvc.perform(get("/swagger-ui.html"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", containsString("swagger-ui/index.html")));
    }

    @Test
    void documentedEndpoints_work() throws Exception {
        mockMvc.perform(post("/api/courses").contentType(MediaType.APPLICATION_JSON).content("{\"name\": \"Spring Advanced\", \"price\": 120}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber());
        mockMvc.perform(post("/api/courses").contentType(MediaType.APPLICATION_JSON).content("{\"name\": \"\"}"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/api/courses")).andExpect(jsonPath("$[0].name").value("Spring Advanced"));
    }
}
