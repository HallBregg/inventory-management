package my.group.productscounter.project;

import jakarta.validation.constraints.NotBlank;

record UpdateProjectRequest(@NotBlank String name) {
}
