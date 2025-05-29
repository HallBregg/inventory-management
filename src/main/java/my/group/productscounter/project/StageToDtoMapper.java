package my.group.productscounter.project;

import my.group.productscounter.project.dto.StageDto;

import java.util.List;

class StageToDtoMapper {
    static StageDto of(Stage stage) {
        return new StageDto(stage.getId(), stage.getName(), StageProductToDtoMapper.of(stage.getProducts()));
    }

    static List<StageDto> of(List<Stage> stages) {
        return stages.stream().map(StageToDtoMapper::of).toList();
    }
}
