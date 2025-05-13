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
    ResponseEntity<ProjectSummaryResponse> createProject(@Valid @RequestBody CreateProjectRequest request) {
        Project project = projectService.createProject(new CreateProjectCommand(request.name()));
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(project.getId()).toUri();
        return ResponseEntity
                .created(location)
                .body(new ProjectSummaryResponse(project.getId(), project.getName()));
    }

    @GetMapping
    List<ProjectSummaryResponse> getAllProjects() {
        return ProjectSummaryResponse.of(projectService.listAllProjects());
    }

    @GetMapping("/{id}")
    ProjectResponse getProject(@PathVariable UUID id) {
        return ProjectResponse.of(projectService.getProject(id));
    }

    @PutMapping("/{id}")
    ProjectSummaryResponse updateProject(@PathVariable UUID id, @Valid @RequestBody UpdateProjectRequest request) {
        return ProjectSummaryResponse
                .of(projectService.updateProject(new UpdateProjectCommand(id, request.name())));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteProject(@PathVariable UUID id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{projectId}/stages")
    StageResponse createStage(@PathVariable UUID projectId, @Valid @RequestBody CreateStageRequest request) {
        return StageResponse.of(projectService.createStage(new CreateStageCommand(projectId, request.name())));
    }

    @PutMapping("/{projectId}/stages/{stageId}")
    StageResponse updateStage(@PathVariable UUID projectId, @PathVariable UUID stageId, @Valid @RequestBody UpdateStageRequest request) {
        return StageResponse.of(projectService.updateStage(new UpdateStageCommand(
                projectId, stageId, request.name(), request.products())));
    }

    @DeleteMapping("/{projectId}/stages/{stageId}")
    ResponseEntity<?> deleteStage(@PathVariable UUID projectId, @PathVariable UUID stageId) {
        projectService.deleteStage(new DeleteStageCommand(projectId, stageId));
        return ResponseEntity.noContent().build();
    }
}
