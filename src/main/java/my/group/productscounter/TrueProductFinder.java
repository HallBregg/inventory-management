package my.group.productscounter;

import my.group.productscounter.project.ProductFinder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;


@Component
@ConditionalOnProperty(name="product.finder.fake", havingValue = "true")
class TrueProductFinder implements ProductFinder {

    @Override
    public boolean existsById(Long productId) {
        return true;
    }
}
