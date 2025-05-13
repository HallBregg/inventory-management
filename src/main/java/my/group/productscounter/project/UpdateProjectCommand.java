package my.group.productscounter.project;

import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;


@Validated
record UpdateProjectCommand(@NotBlank UUID projectId, @NotBlank String name) {
}
