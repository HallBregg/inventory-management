package my.group.productscounter.project;

import java.util.List;
import java.util.UUID;

record UpdateStageCommand(UUID projectId, UUID stageId, String stageName, List<StageProductSpec> products) {
}
