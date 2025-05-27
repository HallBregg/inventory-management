package my.group.productscounter.store.dto;

import java.util.Set;

public record UpdateProductRequest(String name, Set<PropertyDto> properties) {}
