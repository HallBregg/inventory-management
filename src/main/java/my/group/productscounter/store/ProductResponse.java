package my.group.productscounter.store;


import java.util.Collection;

record PropertyResponse(String name, String value){
    static PropertyResponse of(Property property){
        return new PropertyResponse(property.getName(), property.getValue());
    }

    static Collection<PropertyResponse> of(Collection<Property> properties){
        return properties.stream().map(PropertyResponse::of).toList();
    }
}

record ProductResponse(Long id, String name, Collection<PropertyResponse> properties) {
    static ProductResponse of(Product product){
        return new ProductResponse(product.getId(), product.getName(), PropertyResponse.of(product.getProperties()));
    }

    static Collection<ProductResponse> of(Collection<Product> products){
        return products.stream().map(ProductResponse::of).toList();
    }
}
