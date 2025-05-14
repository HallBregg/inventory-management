package my.group.productscounter.store;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

record CreateProductRequest(@NotBlank String name, Set<PropertyData> properties) {
}
