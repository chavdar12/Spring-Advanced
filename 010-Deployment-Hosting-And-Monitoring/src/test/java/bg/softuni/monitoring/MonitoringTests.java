package bg.softuni.monitoring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MonitoringTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void health_isUp() throws Exception {
        mockMvc.perform(get("/actuator/health")).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void info_showsAppAndBuildInformation() throws Exception {
        mockMvc.perform(get("/actuator/info"))
                .andExpect(jsonPath("$.app.name").value("Spring Advanced - Monitoring demo"))
                .andExpect(jsonPath("$.build.artifact").value("deployment-hosting-and-monitoring"));
    }

    @Test
    void customEndpoint_isExposed() throws Exception {
        mockMvc.perform(get("/actuator/custom")).andExpect(status().isOk()).andExpect(jsonPath("$.message").value("My custom endpoint"));
    }

    @Test
    void customMetric_isExportedToPrometheus() throws Exception {
        mockMvc.perform(get("/hello").param("name", "Pesho")).andExpect(content().string("Hello, Pesho!"));

        mockMvc.perform(get("/actuator/metrics/greetings")).andExpect(jsonPath("$.measurements[0].value").value(1.0));
        mockMvc.perform(get("/actuator/prometheus"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("greetings_total 1.0")));
    }

    @Test
    void sensitiveEndpoints_areNotExposed() throws Exception {
        mockMvc.perform(get("/actuator/env")).andExpect(status().isNotFound());
        mockMvc.perform(get("/actuator/beans")).andExpect(status().isNotFound());
    }
}
