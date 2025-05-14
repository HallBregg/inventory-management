package my.group.productscounter.store;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record CreateProductCommand(@NotBlank String name, Set<PropertyData> properties) {
}
