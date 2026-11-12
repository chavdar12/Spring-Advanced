package bg.softuni.demo;

import jakarta.validation.constraints.NotBlank;

public record BookDTO(Long id, @NotBlank String title, @NotBlank String author, @NotBlank String isbn) {

    static BookDTO of(BookEntity entity) {
        return new BookDTO(entity.getId(), entity.getTitle(), entity.getAuthor(), entity.getIsbn());
    }
}
