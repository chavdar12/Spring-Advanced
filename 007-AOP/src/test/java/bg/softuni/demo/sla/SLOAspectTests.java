package bg.softuni.demo.sla;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(OutputCaptureExtension.class)
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

        assertThatThrownBy(() -> aspect.trackLatency(null, latency("missing")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("missing");
    }

    @Test
    void aspect_warns_whenThresholdIsExceeded(CapturedOutput output) throws Throwable {
        Object result = aspectWithThreshold(0).trackLatency(sleepingJoinPoint(5), latency("op"));

        assertThat(result).isEqualTo("done");
        assertThat(output).contains("Method op was too slow");
    }

    @Test
    void aspect_staysQuiet_withinThreshold(CapturedOutput output) throws Throwable {
        Object result = aspectWithThreshold(10_000).trackLatency(sleepingJoinPoint(0), latency("op"));

        assertThat(result).isEqualTo("done");
        assertThat(output).doesNotContain("too slow");
    }

    private static SLOAspect aspectWithThreshold(int threshold) {
        return new SLOAspect(new SLOsConfig().setSlos(List.of(
                new SLOsConfig.SLOConfig().setId("op").setThreshold(threshold))));
    }

    private static ProceedingJoinPoint sleepingJoinPoint(long millis) throws Throwable {
        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        when(pjp.proceed()).thenAnswer(invocation -> {
            Thread.sleep(millis);
            return "done";
        });
        return pjp;
    }

    private static TrackLatency latency(String id) {
        return new TrackLatency() {
            public String latency() { return id; }
            public Class<TrackLatency> annotationType() { return TrackLatency.class; }
        };
    }
}
