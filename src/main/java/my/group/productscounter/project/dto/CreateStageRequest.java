package my.group.productscounter.project.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateStageRequest(@NotBlank String name) {
}
