package bg.softuni.books;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record BookDTO(Long id, @NotBlank String title, @NotBlank String author, @NotBlank String isbn, List<@NotBlank String> tags) {

    static BookDTO of(BookEntity entity) {
        return new BookDTO(entity.getId(), entity.getTitle(), entity.getAuthor(), entity.getIsbn(), List.copyOf(entity.getTags()));
    }
}
