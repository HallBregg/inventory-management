package my.group.productscounter.store;

import jakarta.validation.constraints.NotBlank;

record PropertyData(@NotBlank String name, @NotBlank String value) {
}
