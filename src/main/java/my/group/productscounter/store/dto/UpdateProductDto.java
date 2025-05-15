package my.group.productscounter.store.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record UpdateProductDto(@NotNull Long id, String name, Set<PropertyDto> properties) {
}
