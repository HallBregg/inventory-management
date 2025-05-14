package my.group.productscounter.store;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/api/products")
@Validated
class ProductStoreController {

    private final ProductStoreService productStoreService;

    @Autowired
    ProductStoreController(ProductStoreService productStoreService) {
        this.productStoreService = productStoreService;
    }

    @GetMapping
    Collection<ProductResponse> getAllProducts() {
        return ProductResponse.of(productStoreService.findAll());
    }

    @GetMapping("/{id}")
    ProductResponse getProduct(@PathVariable Long id) {
        return ProductResponse.of(productStoreService.findById(id));
    }

    @PutMapping("/{id}")
    void updateProduct(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest request) {
        productStoreService.update(new UpdateProductCommand(id, request.name(), request.properties()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void createProduct(@Valid @RequestBody CreateProductRequest request) {
        productStoreService.create(new CreateProductCommand(request.name(), request.properties()));
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
