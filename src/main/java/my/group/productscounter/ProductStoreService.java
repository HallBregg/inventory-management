package my.group.productscounter;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
class ProductStoreService {
    private final ProductRepository productRepository;

    @Autowired
    ProductStoreService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    void create(CreateProductCommand command){
        Product product = new Product();
        product.setName(command.name());
        command.properties().forEach(property -> {
            product.addProperty(new Property(property.name(), property.value()));
        });

        productRepository.save(product);
    }

    void delete(Long id){
        productRepository.deleteById(id);
    }

    @Transactional
    void update(UpdateProductCommand command){
        Product product = productRepository
                .findById(command.id())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        if(command.name() != null){
            product.setName(command.name());
        }

        if(command.properties() != null){
            product.getProperties().clear();
            command.properties().forEach(property -> {
                product.addProperty(new Property(property.name(), property.value()));
            });
        }
    }

    List<Product> findAll(){
        return productRepository.findAll();
    }

    List<String> listAllPropertyNames(){
        return productRepository.findAllUniquePropertyNames();
    }
}
