package my.group.productscounter.project.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;


public record UpdateProjectDto(@NotBlank UUID projectId, @NotBlank String name) {
}
