package my.group.productscounter.project;

import jakarta.transaction.Transactional;
import my.group.productscounter.project.dto.*;
import my.group.productscounter.project.exception.ProductNotFoundException;
import my.group.productscounter.project.exception.ProjectCreateException;
import my.group.productscounter.project.exception.ProjectNotFoundException;
import my.group.productscounter.project.exception.ProjectStageUpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProductFinder productFinder;

    @Autowired
    ProjectService(ProjectRepository projectRepository, ProductFinder productFinder) {
        this.projectRepository = projectRepository;
        this.productFinder = productFinder;
    }

    private Project getProjectById(UUID projectId) {
        return projectRepository
                .findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);
    }

    @Transactional
    public ProjectIdentifierDto createProject(CreateProjectDto command) {
        Project project = projectRepository.save(new Project(command.name()));

        try{
            projectRepository.flush();
        } catch (DataAccessException exc){ throw new ProjectCreateException(exc); }

        return new ProjectIdentifierDto(project.getId(), project.getName());
    }

    List<ProjectIdentifierDto> listAllProjects() {
        return ProjectToIdentifierDtoMapper.of(projectRepository.findAll());
    }

    ProjectWithStagesDto getProject(UUID projectId) {
        return ProjectToWithStagesDtoMapper.of(projectRepository
                .findById(projectId)
                .orElseThrow(ProjectNotFoundException::new));
    }

    @Transactional
    ProjectIdentifierDto updateProject(UpdateProjectDto command) {
        Project project = getProjectById(command.projectId());
        project.setName(command.name());
        return ProjectToIdentifierDtoMapper.of(projectRepository.save(project));
    }

    @Transactional
    void deleteProject(UUID projectId) {
        projectRepository.deleteById(projectId);
    }

    @Transactional
    StageDto createStage(CreateStageDto command) {
        Project project = getProjectById(command.projectId());
        Stage stage = project.addStage(command.name());
        projectRepository.flush();
        return StageToDtoMapper.of(stage);
    }

    @Transactional
    StageDto updateStage(UpdateStageDto command) {
        Project project = getProjectById(command.projectId());
        final Stage stage;

        try {
            stage = project.getStage(command.stageId());
        } catch (StageNotFound e) {
            throw new ProjectStageUpdateException(e);
        }

        // Not performant! Maybe some cache on impl. etc.
        command.products().forEach(product -> {
            if (!productFinder.existsById(product.productId())) throw new ProductNotFoundException(product.productId());
        });
        stage.replaceProducts(command.products());
        stage.setName(command.stageName());
        return StageToDtoMapper.of(stage);
    }

    @Transactional
    void deleteStage(DeleteStageDto command) {
        Project project = projectRepository.findById(command.projectId()).orElseThrow(ProjectNotFoundException::new);
        project.deleteStage(command.stageId());
    }
}
