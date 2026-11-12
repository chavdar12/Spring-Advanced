package bg.softuni.errors.product;

public class OutOfStockException extends RuntimeException {

    public OutOfStockException(String productName) {
        super(productName + " is out of stock.");
    }
}
