package my.group.productscounter.infrastructure;

import my.group.productscounter.project.ProductFinder;
import my.group.productscounter.store.ProductStoreService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;


@Component
@ConditionalOnProperty(name = "product.finder.fake", havingValue = "false", matchIfMissing = true)
class StoreProductFinder implements ProductFinder {
    private final ProductStoreService productStoreService;

    StoreProductFinder(ProductStoreService productStoreService) {
        this.productStoreService = productStoreService;
    }

    @Override
    public boolean existsById(Long productId) {
        return productStoreService.exists(productId);
    }
}
