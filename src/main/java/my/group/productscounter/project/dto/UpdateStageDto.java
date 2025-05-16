package my.group.productscounter.project.dto;


import java.util.List;
import java.util.UUID;

public record UpdateStageDto(UUID projectId, UUID stageId, String stageName, List<StageProductDto> products) {
}
