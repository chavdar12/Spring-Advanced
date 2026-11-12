package bg.softuni.errors.web;

import bg.softuni.errors.product.OutOfStockException;
import bg.softuni.errors.product.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProductsController {

    private final ProductService productService;

    public ProductsController(ProductService productService) {
        this.productService = productService;
    }

    // ProductNotFoundException is not handled here: its @ResponseStatus produces a 404 and Boot renders templates/error/404.html
    @GetMapping("/products/{id}")
    public ModelAndView details(@PathVariable Long id) {
        return new ModelAndView("product", "product", productService.getById(id));
    }

    @PostMapping("/products/{id}/buy")
    public String buy(@PathVariable Long id) {
        productService.buy(id);
        return "redirect:/products/" + id;
    }

    // Controller-based handler: only applies to exceptions thrown by this controller.
    @ExceptionHandler(OutOfStockException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ModelAndView outOfStock(OutOfStockException e) {
        return new ModelAndView("product-error", "message", e.getMessage());
    }
}
