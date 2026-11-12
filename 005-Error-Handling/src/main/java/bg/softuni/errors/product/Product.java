package bg.softuni.errors.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record Product(Long id, @NotBlank String name, @PositiveOrZero int stock) {
}
