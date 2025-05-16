package my.group.productscounter.store.dto;

import jakarta.validation.constraints.NotBlank;

public record PropertyDto(@NotBlank String name, @NotBlank String value) {
}
