package my.group.productscounter.project;

import java.util.List;
import java.util.UUID;

record ProjectSummaryResponse(UUID id, String name){

    static ProjectSummaryResponse of(Project project) {
        return new ProjectSummaryResponse(project.getId(), project.getName());
    }

    static List<ProjectSummaryResponse> of(List<Project> projects) {
        return projects.stream().map(ProjectSummaryResponse::of).toList();
    }
};
