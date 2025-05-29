package my.group.productscounter.project;

import my.group.productscounter.project.dto.ProjectIdentifierDto;

import java.util.List;

class ProjectToIdentifierDtoMapper {
    static ProjectIdentifierDto of(Project project) {
        return new ProjectIdentifierDto(project.getId(), project.getName());
    }

    static List<ProjectIdentifierDto> of(List<Project> projects) {
        return projects.stream().map(ProjectToIdentifierDtoMapper::of).toList();
    }
}
