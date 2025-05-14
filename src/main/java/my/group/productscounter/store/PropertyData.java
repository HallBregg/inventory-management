package my.group.productscounter.store;

import jakarta.validation.constraints.NotBlank;

public record PropertyData(@NotBlank String name, @NotBlank String value) {
}
