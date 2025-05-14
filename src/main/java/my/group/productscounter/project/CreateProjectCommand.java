package my.group.productscounter.project;

import jakarta.validation.constraints.NotBlank;


public record CreateProjectCommand(@NotBlank String name) {
}
