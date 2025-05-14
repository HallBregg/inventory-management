package my.group.productscounter.project;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

record DeleteStageCommand(@NotNull UUID projectId, @NotNull UUID stageId) {
}
