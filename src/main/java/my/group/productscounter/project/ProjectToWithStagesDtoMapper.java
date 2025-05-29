package my.group.productscounter.project;

import my.group.productscounter.project.dto.ProjectWithStagesDto;

class ProjectToWithStagesDtoMapper {
    static ProjectWithStagesDto of(Project project) {
        return new ProjectWithStagesDto(project.getId(), project.getName(), StageToDtoMapper.of(project.getStages()));
    }
}
