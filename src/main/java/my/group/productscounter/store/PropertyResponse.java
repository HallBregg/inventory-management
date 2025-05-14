package my.group.productscounter.store;

import java.util.Collection;

record PropertyResponse(String name, String value) {
    static PropertyResponse of(Property property) {
        return new PropertyResponse(property.getName(), property.getValue());
    }

    static Collection<PropertyResponse> of(Collection<Property> properties) {
        return properties.stream().map(PropertyResponse::of).toList();
    }
}
