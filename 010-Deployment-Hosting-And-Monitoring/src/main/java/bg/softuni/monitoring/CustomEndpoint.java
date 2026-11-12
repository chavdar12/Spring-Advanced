package bg.softuni.monitoring;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;

// Custom actuator endpoint: GET /actuator/custom
@Component
@Endpoint(id = "custom")
public class CustomEndpoint {

    private final Instant startedAt = Instant.now();

    @ReadOperation
    public Map<String, Object> custom() {
        return Map.of("message", "My custom endpoint", "startedAt", startedAt.toString());
    }
}
