package bg.softuni.books;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String author;

    private String isbn;

    @ElementCollection(fetch = FetchType.EAGER)
    @OrderColumn // keeps tags in the order the user typed them
    private List<String> tags = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public List<String> getTags() {
        return tags;
    }

    public BookEntity update(BookDTO dto) {
        this.title = dto.title();
        this.author = dto.author();
        this.isbn = dto.isbn();
        this.tags = new ArrayList<>(dto.tags() == null ? List.of() : dto.tags().stream().distinct().toList());
        return this;
    }
}
