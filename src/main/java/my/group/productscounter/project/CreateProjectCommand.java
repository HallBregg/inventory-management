package my.group.productscounter.project;

import jakarta.validation.constraints.NotBlank;


record CreateProjectCommand(@NotBlank String name){};
