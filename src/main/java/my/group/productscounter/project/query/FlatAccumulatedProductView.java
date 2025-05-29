package my.group.productscounter.project.query;

interface FlatAccumulatedProductView {
    Long getProductId();
    String getProductName();
    Integer getQuantity();
    String getPropertyName();
    String getPropertyValue();
}
