package my.group.productscounter.project.dto;

import jakarta.validation.constraints.NotBlank;


public record CreateProjectDto(@NotBlank String name) {
}
