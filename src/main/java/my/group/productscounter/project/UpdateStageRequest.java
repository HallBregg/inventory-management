package my.group.productscounter.project;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

record UpdateStageRequest(@NotBlank String name, List<StageProductSpec> products) {
    public List<StageProductSpec> products() {
        return products == null ? List.of() : products;
    }
}
