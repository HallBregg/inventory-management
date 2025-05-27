package my.group.productscounter.store;

import jakarta.validation.Valid;
import my.group.productscounter.store.dto.CreateProductDto;
import my.group.productscounter.store.dto.ProductDto;
import my.group.productscounter.store.dto.UpdateProductDto;
import my.group.productscounter.store.dto.UpdateProductRequest;
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
    Collection<ProductDto> getAllProducts() {
        return productStoreService.findAll();
    }

    @GetMapping("/{id}")
    ProductDto getProduct(@PathVariable Long id) {
        return productStoreService.findById(id);
    }

    @PutMapping("/{id}")
    ProductDto updateProduct(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest request) {
        return productStoreService.update(new UpdateProductDto(id, request.name(), request.properties()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ProductDto createProduct(@Valid @RequestBody CreateProductDto request) {
        return productStoreService.create(new CreateProductDto(request.name(), request.properties()));
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
