package bg.softuni.demo.sla;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SLOAspectTests {

    @Autowired
    SLOsConfig slosConfig;

    @Autowired
    MockMvc mockMvc;

    @Test
    void thresholdsAreBoundFromConfiguration() {
        assertThat(slosConfig.getSlos())
                .extracting(SLOsConfig.SLOConfig::getId, SLOsConfig.SLOConfig::getThreshold)
                .containsExactly(
                        tuple("local_operation", 60),
                        tuple("remote_operation", 500));
    }

    @Test
    void trackedEndpoint_returnsDrivers() throws Exception {
        mockMvc.perform(get("/drivers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Pesho"))
                .andExpect(jsonPath("$[1].licenceCategory").value("A"));
    }

    @Test
    void aspect_failsFast_forUnconfiguredSlo() {
        SLOAspect aspect = new SLOAspect(new SLOsConfig());
        TrackLatency missing = new TrackLatency() {
            public String latency() { return "missing"; }
            public Class<TrackLatency> annotationType() { return TrackLatency.class; }
        };

        assertThatThrownBy(() -> aspect.trackLatency(null, missing))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("missing");
    }
}
