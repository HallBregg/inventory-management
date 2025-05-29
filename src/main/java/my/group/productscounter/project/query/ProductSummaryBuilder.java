package my.group.productscounter.project.query;

import java.util.LinkedHashMap;
import java.util.Map;

class ProductSummaryBuilder {
    private final Long productId;
    private final String name;
    private final Integer quantity;
    private final Map<String, String> properties = new LinkedHashMap<>();

    ProductSummaryBuilder(Long productId, String name, Integer quantity) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
    }

    void addProperty(String name, String value) {
        this.properties.put(name, value);
    }

    AccumulatedProductView build() {
        return new AccumulatedProductView(productId, name, quantity, properties);
    }
}
