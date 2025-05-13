package my.group.productscounter.project;

import jakarta.validation.constraints.NotBlank;

record CreateStageRequest(@NotBlank String name) {}
