package my.group.productscounter.project.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record UpdateStageRequest(@NotBlank String name, List<StageProductDto> products) {
    public List<StageProductDto> products() {
        return products == null ? List.of() : products;
    }
}
