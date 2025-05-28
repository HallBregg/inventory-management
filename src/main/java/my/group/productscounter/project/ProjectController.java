package my.group.productscounter.project;


import jakarta.validation.Valid;
import my.group.productscounter.project.dto.*;
import my.group.productscounter.project.query.ProjectQuery;
import my.group.productscounter.project.query.ProjectView;
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
    private final ProjectQuery projectQuery;

    @Autowired
    ProjectController(ProjectService projectService, ProjectQuery projectQuery) {
        this.projectService = projectService;
        this.projectQuery = projectQuery;
    }

    @PostMapping
    ResponseEntity<ProjectIdentifierDto> createProject(@Valid @RequestBody CreateProjectDto request) {
        ProjectIdentifierDto dto = projectService.createProject(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.id()).toUri();
        return ResponseEntity
                .created(location)
                .body(dto);
    }

    @GetMapping
    List<ProjectIdentifierDto> getAllProjects() {
        return projectService.listAllProjects();
    }

    @GetMapping("/{id}")
    ProjectWithStagesDto getProject(@PathVariable UUID id) {
        return projectService.getProject(id);
    }

    @PutMapping("/{id}")
    ProjectIdentifierDto updateProject(@PathVariable UUID id, @Valid @RequestBody UpdateProjectRequest request) {
        return projectService.updateProject(new UpdateProjectDto(id, request.name()));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteProject(@PathVariable UUID id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{projectId}/stages")
    StageDto createStage(@PathVariable UUID projectId, @Valid @RequestBody CreateStageRequest request) {
        return projectService.createStage(new CreateStageDto(projectId, request.name()));
    }

    @PutMapping("/{projectId}/stages/{stageId}")
    StageDto updateStage(
            @PathVariable UUID projectId,
            @PathVariable UUID stageId,
            @Valid @RequestBody UpdateStageRequest request
    ) {
        return projectService.updateStage(new UpdateStageDto(projectId, stageId, request.name(), request.products()));
    }

    @DeleteMapping("/{projectId}/stages/{stageId}")
    ResponseEntity<?> deleteStage(@PathVariable UUID projectId, @PathVariable UUID stageId) throws InterruptedException {
        projectService.deleteStage(new DeleteStageDto(projectId, stageId));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/full/{id}")
    ProjectView getProjectView(@PathVariable UUID id){
        return projectQuery.getFullProject(id);
    };

    @GetMapping("/csv/{projectId}")
    ResponseEntity<String> getSummaryCsv(@PathVariable UUID projectId){
        String csv = projectQuery.exportProjectSummaryCSV(projectId);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=summary-" + projectId + ".csv")
                .body(csv);
    }
}
