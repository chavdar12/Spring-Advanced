package bg.softuni.testing.author;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorsController {

    private final AuthorRepository authorRepository;

    public AuthorsController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @GetMapping
    public List<AuthorDTO> getAuthors() {
        return authorRepository.findAll().stream().map(AuthorDTO::of).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthor(@PathVariable Long id) {
        return ResponseEntity.of(authorRepository.findById(id).map(AuthorDTO::of));
    }

    public record AuthorDTO(Long id, String name) {
        static AuthorDTO of(AuthorEntity entity) {
            return new AuthorDTO(entity.getId(), entity.getName());
        }
    }
}
