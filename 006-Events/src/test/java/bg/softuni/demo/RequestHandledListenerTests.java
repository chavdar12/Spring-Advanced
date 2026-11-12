package bg.softuni.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(OutputCaptureExtension.class)
class RequestHandledListenerTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void everyHandledRequest_isLogged(CapturedOutput output) throws Exception {
        mockMvc.perform(get("/create-order"));

        assertThat(output).contains("I have received an Event: ServletRequestHandledEvent: url=[/create-order]");
    }
}
