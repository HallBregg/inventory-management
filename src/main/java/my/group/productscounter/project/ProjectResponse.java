package my.group.productscounter.project;

import java.util.List;
import java.util.UUID;


record ProjectResponse(UUID id, String name, List<StageResponse> stages){
    static ProjectResponse of(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getStages().stream().map(StageResponse::of).toList());
    }
};
