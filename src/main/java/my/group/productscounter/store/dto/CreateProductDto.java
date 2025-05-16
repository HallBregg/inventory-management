package my.group.productscounter.store.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record CreateProductDto(@NotBlank String name, Set<PropertyDto> properties) {
}
