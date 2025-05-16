package my.group.productscounter.store.dto;


import java.util.Set;

public record ProductDto(Long id, String name, Set<PropertyDto> properties) {
}
