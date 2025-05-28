package my.group.productscounter.project.query;

import java.util.Map;

public record ProductSummaryView(Long productId, String name, int quantity, Map<String, String> properties) {
}
