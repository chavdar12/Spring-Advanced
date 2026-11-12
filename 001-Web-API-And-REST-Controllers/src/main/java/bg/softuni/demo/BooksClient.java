package bg.softuni.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

// Consuming a REST API from Java: RestClient is the modern replacement of the RestTemplate shown in the lecture.
@Component
public class BooksClient {

    private final RestClient restClient;

    public BooksClient(RestClient.Builder builder, @Value("${books.api-url:http://localhost:8080}") String apiUrl) {
        this.restClient = builder.baseUrl(apiUrl).build();
    }

    public List<BookDTO> getAll() {
        return restClient.get().uri("/api/books").retrieve().body(new ParameterizedTypeReference<>() {});
    }

    public BookDTO create(BookDTO book) {
        return restClient.post().uri("/api/books").body(book).retrieve().body(BookDTO.class);
    }
}
