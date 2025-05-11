package my.group.productscounter.store;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/products")
@Validated
class ProductController {

    private final ProductStoreService productStoreService;

    @Autowired
    ProductController(ProductStoreService productStoreService) {
        this.productStoreService = productStoreService;
    }

    @GetMapping
    List<Product> getAllProducts() {
        return  productStoreService.findAll();
    }

    @GetMapping("/{id}")
    Product getProductById(@PathVariable Long id) {
        return productStoreService.findById(id);
    }

    @PutMapping("/{id}")
    void updateProduct(@PathVariable int id, @Valid @RequestBody UpdateProductCommand updateProductCommand) {
        productStoreService.update(updateProductCommand);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void createProduct(
            @Valid @RequestBody CreateProductCommand createProductCommand) {
        productStoreService.create(createProductCommand);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteProduct(@PathVariable Long id) {
        productStoreService.delete(id);
    }

    @GetMapping("/properties")
    List<String> getAllProductsProperties() {
        return productStoreService.listAllPropertyNames();
    }
}
