package bg.softuni.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BooksClientTests {

    @LocalServerPort
    int port;

    @Autowired
    RestClient.Builder builder;

    @Test
    void client_readsAndCreatesBooksOverHttp() {
        BooksClient client = new BooksClient(builder, "http://localhost:" + port);

        BookDTO created = client.create(new BookDTO(null, "Refactoring", "Martin Fowler", "9780134757599"));

        assertThat(created.id()).isNotNull();
        assertThat(client.getAll()).extracting(BookDTO::title).contains("Spring in Action", "Refactoring");
    }
}
