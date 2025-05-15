package my.group.productscounter.store;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class ProductStoreService {
    private final ProductRepository productRepository;

    @Autowired
    ProductStoreService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    Product create(CreateProductCommand command) {
        Product product = new Product();
        product.setName(command.name());
        command.properties().forEach(property -> {
            product.addProperty(new Property(property.name(), property.value()));
        });

        try {
            return productRepository.save(product);
        } catch (Exception e) {
            throw new ProductCouldNotBeCreatedException();
        }
    }

    void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Transactional
    void update(UpdateProductCommand command) {
        Product product = productRepository
                .findById(command.id())
                .orElseThrow(ProductNotFoundException::new);

        if (command.name() != null) {
            product.setName(command.name());
        }

        if (command.properties() != null) {
            product.getProperties().clear();
            command.properties().forEach(property -> {
                product.addProperty(new Property(property.name(), property.value()));
            });
        }
    }

    Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
    }

    List<Product> findAll() {
        return productRepository.findAll();
    }

    List<String> listAllPropertyNames() {
        return productRepository.findAllUniquePropertyNames();
    }

    public boolean exists(Long id) {
        return productRepository.existsById(id);
    }
}
