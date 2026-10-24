package bg.softuni.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RecordApplicationEvents
@ExtendWith(OutputCaptureExtension.class)
class OrderEventsTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ApplicationEvents events;

    @Test
    void createOrder_publishesOneEvent_handledByBothListeners(CapturedOutput output) throws Exception {
        mockMvc.perform(get("/create-order"))
                .andExpect(status().isOk())
                .andExpect(content().string("test"));

        assertThat(events.stream(OrderCreatedEvent.class)).hasSize(1);
        String orderId = events.stream(OrderCreatedEvent.class).findFirst().orElseThrow().getOrderId();

        assertThat(output).contains("Order no " + orderId + " has been created. I'm going to give bonus points");
        assertThat(output).contains("Order no " + orderId + " has been created. I'm going to calculate");
    }

    @Test
    void eachOrder_getsADistinctId() throws Exception {
        mockMvc.perform(get("/create-order"));
        mockMvc.perform(get("/create-order"));

        assertThat(events.stream(OrderCreatedEvent.class).map(OrderCreatedEvent::getOrderId).distinct()).hasSize(2);
    }
}
