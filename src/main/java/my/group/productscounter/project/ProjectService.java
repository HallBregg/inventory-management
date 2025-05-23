package my.group.productscounter.project;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import my.group.productscounter.project.dto.*;
import my.group.productscounter.project.exception.ProductNotFoundException;
import my.group.productscounter.project.exception.ProjectCreateException;
import my.group.productscounter.project.exception.ProjectNotFoundException;
import my.group.productscounter.project.exception.ProjectStageUpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

class StageProductToDtoMapper{
    static StageProductDto of(StageProduct product){
        return new StageProductDto(product.getProductId(), product.getPosition().intValue(), product.getQuantity());
    }

    static List<StageProductDto> of(List<StageProduct> products){
        return products.stream().map(StageProductToDtoMapper::of).toList();
    }
}

class StageToDtoMapper{
    static StageDto of(Stage stage){
        return new StageDto(stage.getId(), stage.getName(), StageProductToDtoMapper.of(stage.getProducts()));
    }

    static List<StageDto> of(List<Stage> stages){
        return stages.stream().map(StageToDtoMapper::of).toList();
    }
}

class ProjectToIdentifierDtoMapper{
    static ProjectIdentifierDto of(Project project){
        return new ProjectIdentifierDto(project.getId(), project.getName());
    }

    static List<ProjectIdentifierDto> of(List<Project> projects){
        return projects.stream().map(ProjectToIdentifierDtoMapper::of).toList();
    }
}

class ProjectToWithStagesDtoMapper{
    static ProjectWithStagesDto of(Project project){
        return new ProjectWithStagesDto(project.getId(), project.getName(), StageToDtoMapper.of(project.getStages()));
    }
}

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
