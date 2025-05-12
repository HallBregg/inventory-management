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

    @GetMapping("/{id}")
    ProjectResponse fetchProject(@PathVariable UUID id) {
        Project project = projectService.findById(id);

        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project
                        .getStages()
                        .stream()
                        .map(stage -> new StageResponse(
                                stage.getId(),
                                stage.getName(),
                                stage.getProducts()
                                        .stream()
                                        .map(stageProduct -> new StageProductResponse(
                                                stageProduct.getProductId(),
                                                stageProduct.getPosition().getValue(),
                                                stageProduct.getQuantity())
                                        ).toList())
                        ).toList());
    }

    @GetMapping
    List<ProjectSummaryResponse> fetchProjects() {
        return projectService
                .findAll()
                .stream()
                .map(project -> new ProjectSummaryResponse(
                        project.getId(), project.getName())).toList();
    }
}
