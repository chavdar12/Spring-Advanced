package bg.softuni.demo;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookDTO> findAll() {
        return bookRepository.findAll().stream().map(BookDTO::of).toList();
    }

    public Optional<BookDTO> findById(Long id) {
        return bookRepository.findById(id).map(BookDTO::of);
    }

    public BookDTO create(BookDTO book) {
        if (bookRepository.existsByIsbn(book.isbn())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A book with ISBN " + book.isbn() + " already exists.");
        }
        return BookDTO.of(bookRepository.save(copy(book, new BookEntity())));
    }

    public Optional<BookDTO> update(Long id, BookDTO book) {
        return bookRepository.findById(id).map(entity -> BookDTO.of(bookRepository.save(copy(book, entity))));
    }

    public boolean delete(Long id) {
        if (!bookRepository.existsById(id)) {
            return false;
        }
        bookRepository.deleteById(id);
        return true;
    }

    private static BookEntity copy(BookDTO from, BookEntity to) {
        return to.setTitle(from.title()).setAuthor(from.author()).setIsbn(from.isbn());
    }
}
