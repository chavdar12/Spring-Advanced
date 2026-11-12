package bg.softuni.errors.product;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Annotated custom exception: when nothing else handles it, Spring answers with 404 and this reason.
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Product was not found.")
public class ProductNotFoundException extends RuntimeException {

    private final Long productId;

    public ProductNotFoundException(Long productId) {
        super("Product with id " + productId + " was not found.");
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
