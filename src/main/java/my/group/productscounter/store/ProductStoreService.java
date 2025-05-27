package my.group.productscounter.store;

import jakarta.transaction.Transactional;
import my.group.productscounter.store.dto.CreateProductDto;
import my.group.productscounter.store.dto.ProductDto;
import my.group.productscounter.store.dto.PropertyDto;
import my.group.productscounter.store.dto.UpdateProductDto;
import my.group.productscounter.store.exception.ProductCouldNotBeCreatedException;
import my.group.productscounter.store.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


class ProductToDtoMapper {
    static ProductDto of(Product product){
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getProperties()
                        .stream()
                        .map(property -> new PropertyDto(
                                property.getName(),
                                property.getValue()))
                        .collect(Collectors.toSet()));
    };
}


//@Validated
@Service
public class ProductStoreService {
    private final ProductRepository productRepository;

    @Autowired
    ProductStoreService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    ProductDto create(CreateProductDto command) {
        Product product;
        try{
            product = new Product(command.name());
            command.properties().forEach(property -> {product.addProperty(property.name(), property.value());});
        } catch (Exception exc) {
            throw new ProductCouldNotBeCreatedException(exc);
        }
        productRepository.save(product);
        return ProductToDtoMapper.of(product);
    }

    void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Transactional
    ProductDto update(UpdateProductDto command) {
        Product product = productRepository
                .findById(command.id())
                .orElseThrow(ProductNotFoundException::new);

        if (command.name() != null) {
            product.setName(command.name());
        }

        if (command.properties() != null) {
            product.getProperties().clear();
            command.properties().forEach(property -> {product.addProperty(property.name(), property.value());});
        }
        return ProductToDtoMapper.of(product);
    }

    ProductDto findById(Long id) {
        return ProductToDtoMapper.of(productRepository
                .findById(id)
                .orElseThrow(ProductNotFoundException::new));
    }

    List<ProductDto> findAll() {
        return productRepository.findAll().stream().map(ProductToDtoMapper::of).toList();

    }

    List<String> listAllPropertyNames() {
        return productRepository.findAllUniquePropertyNames();
    }

    public boolean exists(Long id) {
        return productRepository.existsById(id);
    }
}
