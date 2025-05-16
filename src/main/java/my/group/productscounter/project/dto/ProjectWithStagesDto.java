package my.group.productscounter.project.dto;

import java.util.List;
import java.util.UUID;

public record ProjectWithStagesDto(UUID id, String name, List<StageDto> stages) {
}
