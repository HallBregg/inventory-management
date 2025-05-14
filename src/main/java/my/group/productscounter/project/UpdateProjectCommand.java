package my.group.productscounter.project;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;


record UpdateProjectCommand(@NotBlank UUID projectId, @NotBlank String name) {
}
