package my.group.productscounter.project;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;


@RestController()
@RequestMapping("/api/projects")
@Validated
class ProjectController {
    private final ProjectService projectService;

    @Autowired
    ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    ResponseEntity<ProjectSummaryResponse> createProject(@Valid @RequestBody CreateProjectCommand createProjectCommand) {
        Project project = projectService.createProject(createProjectCommand);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(project.getId()).toUri();
        return ResponseEntity
                .created(location)
                .body(new ProjectSummaryResponse(project.getId(), project.getName()));
    }

    @PutMapping("/{id}")
    ProjectSummaryResponse updateProject(@PathVariable UUID id, @Valid @RequestBody UpdateProjectCommand updateProjectCommand) {
        return ProjectSummaryResponse.of(projectService.updateProject(updateProjectCommand));
    }

    @GetMapping("/{id}")
    ProjectResponse fetchProject(@PathVariable UUID id) {
        return ProjectResponse.of(projectService.getProject(id));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteProject(@PathVariable UUID id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    List<ProjectSummaryResponse> fetchProjects() {
        return ProjectSummaryResponse.of(projectService.listAllProjects());
    }

    @PostMapping("/{projectId}/stages")
    StageSummaryResponse createStage(@PathVariable UUID projectId, @Valid @RequestBody CreateStageCommand createStageCommand) {
        return StageSummaryResponse.of(projectService.createStage(createStageCommand));
    }

}
