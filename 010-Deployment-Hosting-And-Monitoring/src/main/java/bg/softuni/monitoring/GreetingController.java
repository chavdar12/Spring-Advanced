package bg.softuni.monitoring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private final Counter greetings;

    public GreetingController(MeterRegistry meterRegistry) {
        // custom business metric, visible in /actuator/metrics/greetings and /actuator/prometheus as greetings_total
        this.greetings = Counter.builder("greetings").description("Number of greetings served").register(meterRegistry);
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(defaultValue = "World") String name) {
        greetings.increment();
        return "Hello, " + name + "!";
    }
}
