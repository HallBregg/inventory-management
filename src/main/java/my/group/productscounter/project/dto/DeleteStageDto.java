package my.group.productscounter.project.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DeleteStageDto(@NotNull UUID projectId, @NotNull UUID stageId) {
}
