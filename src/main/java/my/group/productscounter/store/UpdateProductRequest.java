package my.group.productscounter.store;

import java.util.Set;

record UpdateProductRequest(String name, Set<PropertyData> properties) {
}
