package my.group.productscounter.project;

import jakarta.validation.constraints.NotBlank;


record CreateProjectRequest(@NotBlank String name){}
