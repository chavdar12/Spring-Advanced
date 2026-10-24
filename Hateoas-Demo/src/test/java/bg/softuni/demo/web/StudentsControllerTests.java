package bg.softuni.demo.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StudentsControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void getStudents_returnsSeededStudentsWithLinks() throws Exception {
        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.studentDTOList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.studentDTOList[0].name").value("Pesho"))
                .andExpect(jsonPath("$._embedded.studentDTOList[0]._links.self.href", endsWith("/students/1")))
                .andExpect(jsonPath("$._embedded.studentDTOList[0]._links.orders.href", endsWith("/students/1/orders")))
                .andExpect(jsonPath("$._embedded.studentDTOList[0]._links.update.href", endsWith("/students/1")));
    }

    @Test
    void getStudentById_returnsStudent() throws Exception {
        mockMvc.perform(get("/students/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Anna"))
                .andExpect(jsonPath("$.age").value(23));
    }

    @Test
    void getOrders_returnsOrdersOfStudent() throws Exception {
        mockMvc.perform(get("/students/1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.orders", hasSize(2)))
                .andExpect(jsonPath("$._embedded.orders[0].studentId").value(1));
    }

    @Test
    void unknownStudent_returns404() throws Exception {
        mockMvc.perform(get("/students/999")).andExpect(status().isNotFound());
        mockMvc.perform(get("/students/999/orders")).andExpect(status().isNotFound());
    }
}
