package my.group.productscounter;

import my.group.productscounter.project.ProductFinder;
import my.group.productscounter.store.ProductStoreService;
import org.springframework.stereotype.Component;


@Component
public class StoreProductFinder implements ProductFinder {
    private final ProductStoreService productStoreService;

    StoreProductFinder(ProductStoreService productStoreService){
        this.productStoreService = productStoreService;
    }

    @Override
    public boolean existsById(Long productId) {
        return productStoreService.exists(productId);
    }
}
