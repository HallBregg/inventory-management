package my.group.productscounter.store;


import jakarta.validation.constraints.NotNull;

import java.util.Set;

record UpdateProductCommand(@NotNull Long id, String name, Set<PropertyData> properties) {
}

