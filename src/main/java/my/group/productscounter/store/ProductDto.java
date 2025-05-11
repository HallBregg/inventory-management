package my.group.productscounter.store;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

record PropertyDto(@NotBlank String name, @NotBlank String value){};

record CreateProductCommand(@NotBlank String name, Set<PropertyDto> properties){};

record UpdateProductCommand(@NotNull Long id, String name, Set<PropertyDto> properties){};

record UpdateProductRequest(String name, Set<PropertyDto> properties){};

record CreateProductRequest(@NotBlank String name, Set<PropertyDto> properties){};
