package bg.softuni.errors.product;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductService {

    private final Map<Long, Product> products = new ConcurrentHashMap<>(Map.of(
            1L, new Product(1L, "Keyboard", 3),
            2L, new Product(2L, "Mouse", 0)));
    private final AtomicLong nextId = new AtomicLong(3);

    public Product getById(Long id) {
        Product product = products.get(id);
        if (product == null) {
            throw new ProductNotFoundException(id);
        }
        return product;
    }

    public Product buy(Long id) {
        return products.compute(id, (key, product) -> {
            if (product == null) {
                throw new ProductNotFoundException(id);
            }
            if (product.stock() == 0) {
                throw new OutOfStockException(product.name());
            }
            return new Product(id, product.name(), product.stock() - 1);
        });
    }

    public Product create(Product product) {
        long id = nextId.getAndIncrement();
        Product created = new Product(id, product.name(), product.stock());
        products.put(id, created);
        return created;
    }
}
