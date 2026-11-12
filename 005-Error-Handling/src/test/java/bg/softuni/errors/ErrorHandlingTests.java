package bg.softuni.errors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ErrorHandlingTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void responseStatusException_producesItsStatusAndReason() throws Exception {
        mockMvc.perform(get("/products/42"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Product was not found."));
    }

    @Test
    void controllerExceptionHandler_rendersErrorView() throws Exception {
        mockMvc.perform(post("/products/2/buy"))
                .andExpect(status().isConflict())
                .andExpect(view().name("product-error"))
                .andExpect(content().string(containsString("Error: Mouse is out of stock.")));
    }

    @Test
    void happyPath_buysProduct() throws Exception {
        mockMvc.perform(post("/products/1/buy")).andExpect(status().is3xxRedirection());
        mockMvc.perform(get("/products/1")).andExpect(content().string(containsString("In stock: 2")));
    }

    @Test
    void globalAdvice_overridesResponseStatus_forRestControllers() throws Exception {
        mockMvc.perform(get("/api/products/42"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.url").value("/api/products/42"))
                .andExpect(jsonPath("$.message").value("Product with id 42 was not found."));
        mockMvc.perform(post("/api/products/2/buy"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Mouse is out of stock."));
    }

    @Test
    void globalAdvice_reportsValidationErrorsPerField() throws Exception {
        mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content("{\"name\": \"\", \"stock\": -1}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed."))
                .andExpect(jsonPath("$.fieldErrors.name").exists())
                .andExpect(jsonPath("$.fieldErrors.stock").exists());
        mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content("{\"name\": \"Monitor\", \"stock\": 5}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3));
    }
}
