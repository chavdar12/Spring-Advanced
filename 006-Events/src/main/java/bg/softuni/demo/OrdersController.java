package bg.softuni.demo;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class OrdersController {

    private final ApplicationEventPublisher eventPublisher;

    public OrdersController(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @GetMapping("/create-order")
    public String createOrder() {

        OrderCreatedEvent event = new OrderCreatedEvent(this, UUID.randomUUID().toString());

        eventPublisher.publishEvent(event);

        return "test";
    }
}
