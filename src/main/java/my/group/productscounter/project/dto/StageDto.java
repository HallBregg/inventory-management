package my.group.productscounter.project.dto;

import java.util.List;
import java.util.UUID;

public record StageDto(UUID id, String name, List<StageProductDto> products) {
}
