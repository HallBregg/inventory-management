package my.group.productscounter.project.query;

import java.util.UUID;

interface FlatProjectSummary {
    Long getProductId();
    String getProductName();
    Integer getQuantity();
    String getPropertyName();
    String getPropertyValue();
}
